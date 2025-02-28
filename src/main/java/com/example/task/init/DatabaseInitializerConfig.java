package com.example.task.init;


import com.example.task.model.Task;
import com.example.task.model.status.TaskStatus;
import com.example.task.model.user.Role;
import com.example.task.model.user.RoleType;
import com.example.task.model.user.User;
import com.example.task.repository.TaskRepository;
import com.example.task.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.time.Instant;

import java.util.Collections;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DatabaseInitializerConfig {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<Void> deleteAllUsers() {
        return userRepository.deleteAll();
    }
    @PostConstruct
    public void initializeDatabase() throws InterruptedException {
        this.deleteAllUsers().block();
        Thread.sleep(1000);
        User admin = User.builder()
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin"))
                .roles(Collections.singleton(Role.from(RoleType.ROLE_MANAGER)))
                .build();
        admin = userRepository.save(admin).block();
        User user = User.builder()
                .username("user")
                .email("user@example.com")
                .password(passwordEncoder.encode("user123"))
                .roles(Set.of(Role.from(RoleType.ROLE_USER)))
                .build();
        user = userRepository.save(user).block();

        assert admin != null;
        System.out.println(userRepository.findById(admin.getId()).blockOptional().orElseThrow().getId());
        assert user != null;
        Task task1 = Task.builder()
                .name("First Task")
                .description("This is the first task.")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .status(TaskStatus.IN_PROGRESS)
                .authorId(admin.getId())
                .assigneeId(user.getId())
                .observerIds(Set.of(user.getId()))
                .build();
        taskRepository.save(task1);

        Task task2 = Task.builder()
                .name("Second Task")
                .description("This is the second task.")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .status(TaskStatus.TO_DO)
                .authorId(user.getId())
                .assigneeId(admin.getId())
                .observerIds(Set.of(admin.getId()))
                .build();
        taskRepository.save(task2);
    }
}