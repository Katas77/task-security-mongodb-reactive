package com.example.task.service;

import com.example.task.exception.MyException;
import com.example.task.model.Task;
import com.example.task.model.user.User;
import com.example.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Transactional
    public Flux<Task> findAll() {
        return taskRepository.findAll().flatMap(this::enhanceTask);
    }

    @Transactional
    public Mono<Task> findById(String id) {
        return taskRepository.findById(id).flatMap(this::enhanceTask);
    }

    public Mono<Task> save(Task task) {
        task.setId(UUID.randomUUID().toString().substring(0, 4));
        Instant now = Instant.now();
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        return Mono.zip(
                        findUser(task.getAuthorId()),
                        findUser(task.getAssigneeId()),
                        observersSet(task.getObserverIds())
                )
                .flatMap(tuple -> {
                    task.setAuthor(tuple.getT1());
                    task.setAssignee(tuple.getT2());
                    task.setObservers(tuple.getT3());
                    return taskRepository.save(task);
                });
    }

    public Mono<Set<User>> observersSet(Set<String> observerIds) {
        return Flux.fromIterable(observerIds)
                .flatMap(this::findUser)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Mono<User> findUser(String userId) {
        return userService.findById(userId);
    }

    public Mono<Task> update(String id, Task task) {
        return findById(id)
                .flatMap(existingTask -> {
                    if (task == null) {
                        return Mono.error(new MyException("Не предоставлены данные для обновления задачи"));
                    }
                    updateTaskFields(existingTask, task);
                    existingTask.setUpdatedAt(Instant.now());
                    return taskRepository.save(existingTask);
                });
    }

    public Mono<Task> addObserver(String taskId, String observerId) {
        return findById(taskId)
                .flatMap(task -> {
                    task.getObserverIds().add(observerId);
                    task.setUpdatedAt(Instant.now());
                    return taskRepository.save(task);
                });
    }

    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }
    private Mono<Task> enhanceTask(Task task) {
        return Mono.zip(
                userService.findById(task.getAuthorId()),
                userService.findById(task.getAssigneeId()),
                observersSet(task.getObserverIds())
        ).map(tuple -> {
            return Task.builder()
                    .id(task.getId())
                    .name(task.getName())
                    .description(task.getDescription())
                    .createdAt(task.getCreatedAt())
                    .updatedAt(task.getUpdatedAt())
                    .status(task.getStatus())
                    .authorId(task.getAuthorId())
                    .assigneeId(task.getAssigneeId())
                    .author(tuple.getT1())
                    .assignee(tuple.getT2())
                    .observers(tuple.getT3())
                    .build();
        });
    }
    /**Метод .zip() в проекте Reactor позволяет объединять результаты нескольких асинхронных операций (например, вызовы к базе данных или веб-сервисам),
     которые выполняются параллельно, и ожидать успешного выполнения всех этих операций одновременно. После завершения всех операций, метод объединяет результаты в одну структуру данных, такую как TupleN
     (где N - количество объединяемых потоков).*/

    private void updateTaskFields(Task existingTask, Task updatedTask) {
        if (updatedTask.getName() != null) existingTask.setName(updatedTask.getName());
        if (updatedTask.getDescription() != null) existingTask.setDescription(updatedTask.getDescription());
        if (updatedTask.getStatus() != null) existingTask.setStatus(updatedTask.getStatus());
        if (updatedTask.getAuthorId() != null) existingTask.setAuthorId(updatedTask.getAuthorId());
        if (updatedTask.getAssigneeId() != null) existingTask.setAssigneeId(updatedTask.getAssigneeId());
        if (updatedTask.getObserverIds() != null) existingTask.setObserverIds(updatedTask.getObserverIds());
    }
}









