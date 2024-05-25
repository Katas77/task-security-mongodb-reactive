package com.example.TasktrackerSecurity.controller;


import com.example.TasktrackerSecurity.entity.Task;
import com.example.TasktrackerSecurity.mapper.TaskMapper;
import com.example.TasktrackerSecurity.model.TaskModel;
import com.example.TasktrackerSecurity.model.TaskModelCreate;
import com.example.TasktrackerSecurity.model.TaskModelUpdate;
import com.example.TasktrackerSecurity.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @PreAuthorize("hasAnyRole ('MANAGER', 'USER')")
    @GetMapping
    public Flux<TaskModel> getAllTasks() {
        return taskService.findAll().map(taskMapper::taskToTaskModel);
    }

    @PreAuthorize("hasAnyRole ('MANAGER', 'USER')")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskModel>> getById(@PathVariable String id) {
        return taskService.findById(id)
                .map(taskMapper::taskToTaskModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole ('MANAGER')")
    @PostMapping
    public Mono<ResponseEntity<Task>> createTask(@RequestBody TaskModelCreate model, @AuthenticationPrincipal Mono<Principal> principal) throws ExecutionException, InterruptedException {
        return taskService.save(taskMapper.taskModelCreateToTask(model, principal))
                .map(ResponseEntity::ok);

    }

    @PreAuthorize("hasAnyRole ('MANAGER')")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskModelUpdate>> updateItem(@PathVariable String id, @RequestBody TaskModelUpdate model) {
        return taskService.update(id, taskMapper.taskModelUpdateToTask(model))
                .map(taskMapper::TaskToTaskModelUpdate)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @PreAuthorize("hasAnyRole ('MANAGER', 'USER')")
    @PutMapping("/{idTask}/{observerId}")
    public Mono<ResponseEntity<TaskModel>> addObserver(@PathVariable String idTask, @PathVariable String observerId) {
        return taskService.addObserver(idTask, observerId)
                .map(taskMapper::taskToTaskModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @PreAuthorize("hasAnyRole ('MANAGER')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItem(@PathVariable String id) {
        return taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));

    }


}
