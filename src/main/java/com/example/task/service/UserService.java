package com.example.task.service;

import com.example.task.exception.MyException;
import com.example.task.model.user.Role;
import com.example.task.model.user.RoleType;
import com.example.task.model.user.User;
import com.example.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id).switchIfEmpty(Mono.error(new MyException("Пользователь не найден")));
    }

    public Mono<User> findByName(String name) {
        return userRepository.findByUsername(name);
    }

    public Mono<User> save(User user, RoleType roleType) {
        if (user == null) {
            return Mono.error(new IllegalArgumentException("User data must not be null"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(UUID.randomUUID().toString().substring(0, 4));
        user.setRoles(Collections.singleton(Role.from(roleType)));

        return userRepository.save(user);
    }

    public Mono<User> update(String id, User updatedUser) {
        if (updatedUser == null) {
            return Mono.error(new IllegalArgumentException("Updated user data must not be null"));
        }

        return findById(id)
                .flatMap(existingUser -> {
                    if (!StringUtils.hasText(updatedUser.getUsername()) && updatedUser.getEmail() == null) {
                        return Mono.error(new IllegalArgumentException("No data provided for updating the user"));
                    }
                    if (StringUtils.hasText(updatedUser.getUsername())) {
                        existingUser.setUsername(updatedUser.getUsername());
                    }
                    if (updatedUser.getEmail() != null) {
                        existingUser.setEmail(updatedUser.getEmail());
                    }
                    return userRepository.save(existingUser);
                });
    }

    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }

    public Mono<Boolean> sendEmail(String userId) {
        return findById(userId)
                .flatMap(user -> {
                    log.info("Sending email to: {}", user.getEmail());

                    return Mono.just(true);
                })
                .doOnError(exception -> log.warn("Failed to send email: {}", exception.getMessage()))
                .defaultIfEmpty(false);
    }
}
