package com.example.task.controller;

import com.example.task.model.Task;
import com.example.task.mapper.TaskMapper;
import com.example.task.dto.TaskModel;
import com.example.task.dto.TaskModelCreate;
import com.example.task.dto.TaskModelUpdate;
import com.example.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    @GetMapping
    public Flux<TaskModel> getAllTasks() {
        return taskService.findAll()
                .map(taskMapper::taskToTaskModel);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskModel>> getById(@PathVariable String id) {
        return taskService.findById(id)
                .map(taskMapper::taskToTaskModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PostMapping
    public Mono<ResponseEntity<Task>> createTask(
            @RequestBody TaskModelCreate model,
            @AuthenticationPrincipal Mono<Principal> principal
    ) throws ExecutionException, InterruptedException {
        return taskService.save(taskMapper.taskModelCreateToTask(model))
                .map(ResponseEntity::ok);
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskModelUpdate>> updateItem(
            @PathVariable String id,
            @RequestBody TaskModelUpdate model
    ) {
        return taskService.update(id, taskMapper.taskModelUpdateToTask(model))
                .map(taskMapper::taskToTaskModelUpdate)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    @PutMapping("/{idTask}/{observerId}")
    public Mono<ResponseEntity<TaskModel>> addObserver(
            @PathVariable String idTask,
            @PathVariable String observerId
    ) {
        return taskService.addObserver(idTask, observerId)
                .map(taskMapper::taskToTaskModel)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItem(@PathVariable String id) {
        return taskService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}