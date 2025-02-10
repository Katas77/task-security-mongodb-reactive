package com.example.task.service;


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
        return userRepository.findById(id);
    }


    public Mono<User> findByName(String name) {
        return userRepository.findByUsername(name);

    }

    public Mono<User> save(User user, RoleType roleType) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(UUID.randomUUID().toString().substring(0, 4));
        user.setRoles(Collections.singleton(Role.from(roleType)));
        return userRepository.save(user);
    }

    public Mono<User> update(String id, User updatedUser) {
        return findById(id)
                .zipWith(Mono.justOrEmpty(updatedUser))
                .flatMap(tuple -> {
                    User existingUser = tuple.getT1();
                    User newUserData = tuple.getT2();
                    if (!StringUtils.hasText(newUserData.getUsername()) && newUserData.getEmail() == null) {
                        return Mono.error(new IllegalArgumentException("No data provided for updating the user"));
                    }
                    if (StringUtils.hasText(newUserData.getUsername())) {
                        existingUser.setUsername(newUserData.getUsername());
                    }
                    if (newUserData.getEmail() != null) {
                        existingUser.setEmail(newUserData.getEmail());
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
                    log.info("Sending email to: " + user.getEmail());
                    return Mono.just(true);
                })
                .defaultIfEmpty(false);
    }


}
