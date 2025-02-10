package com.example.task.controller;


import com.example.task.model.user.RoleType;
import com.example.task.model.user.User;
import com.example.task.mapper.UserMapper;
import com.example.task.dto.UserModel;
import com.example.task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final UserMapper userMapper;


    @GetMapping
    public Flux<User> getAllItems() {
        return userService.findAll();
    }

    @PreAuthorize("hasAnyRole ('MANAGER')")
    @GetMapping("/admin/{id}")
    public Mono<ResponseEntity<UserModel>> getById(@PathVariable String id, @AuthenticationPrincipal Mono<Principal> principal) {
        return userService.findById(id)
                .map(userMapper::userToModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole ('MANAGER', 'USER')")
    @GetMapping("/admin/by-name")
    public Mono<ResponseEntity<UserModel>> getUserByName(@RequestParam String name) {
        return userService.findByName(name)
                .map(userMapper::userToModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<UserModel>> createUser(@RequestBody UserModel model, @RequestParam RoleType roleType) {
        return userService.save(userMapper.modelToUser(model), roleType)
                .map(userMapper::userToModel)
                .map(ResponseEntity::ok);

    }

    @PreAuthorize("hasAnyRole ('MANAGER', 'USER')")
    @PutMapping("/admin/{id}")
    public Mono<ResponseEntity<UserModel>> updateUser(@PathVariable String id, @RequestBody UserModel model) {
        return userService.update(id, userMapper.modelToUser(model))
                .map(userMapper::userToModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole ('MANAGER', 'USER')")
    @DeleteMapping("/admin/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return userService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }


}
