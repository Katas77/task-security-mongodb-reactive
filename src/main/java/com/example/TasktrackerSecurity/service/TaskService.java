package com.example.TasktrackerSecurity.service;


import com.example.TasktrackerSecurity.entity.Task;
import com.example.TasktrackerSecurity.entity.user.User;
import com.example.TasktrackerSecurity.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Transactional
    public Flux<Task> findAll() {
        return taskRepository.findAll()
                .flatMap(this::zipLocAuthor)
                .flatMap(this::zipLocAssignee)
                .flatMap(this::zipLocSet);
    }

    @Transactional
    public Mono<Task> findById(String id) {
        return taskRepository.findById(id)
                .flatMap(this::zipLocAuthor)
                .flatMap(this::zipLocAssignee)
                .flatMap(this::zipLocSet);
    }

    public Mono<Task> save(Task task) {
        String id = UUID.randomUUID().toString().substring(0, 4);
        task.setId(id);
        task.setCreatedAt(Instant.now());
        task.setAuthor(findUser(task.getAuthorId()));
        task.setAssignee(findUser(task.getAssigneeId()));
        task.setObservers(observersSet(task.getObserverIds()));
        return taskRepository.save(task);
    }

    public Mono<Task> update(String id, Task task) {
        task.setUpdatedAt(Instant.now());
        return findById(id).flatMap(taskForUpdate -> {
            if (task.getName() != null) {
                taskForUpdate.setName(task.getName());
            }
            if (task.getDescription() != null) {
                taskForUpdate.setDescription(task.getDescription());
            }
            if (task.getUpdatedAt() != null) {
                taskForUpdate.setUpdatedAt(task.getUpdatedAt());
            }
            if (task.getStatus() != null) {
                taskForUpdate.setStatus(task.getStatus());
            }
            if (task.getAuthorId() != null) {
                taskForUpdate.setAuthorId(task.getAuthorId());
            }
            if (task.getAssigneeId() != null) {
                taskForUpdate.setAssigneeId(task.getAssigneeId());
            }
            if (task.getObserverIds() != null) {
                taskForUpdate.setObserverIds(task.getObserverIds());
            }
            return taskRepository.save(taskForUpdate);
        });

    }

    public Mono<Task> addObserver(String idTask, String observerId) {
        Set<String> strings = new HashSet<>();
        if (findUser(observerId) != null) {
            strings.add(observerId);
        }
        strings.add(observerId);
        return findById(idTask).flatMap(taskForUpdate -> {
            taskForUpdate.setUpdatedAt(Instant.now());
            strings.addAll(taskForUpdate.getObserverIds());
            taskForUpdate.setObserverIds(strings);
            return taskRepository.save(taskForUpdate);
        });
    }

    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }

    public User findUser(String userId) {
        User user = new User();
        try {
            user = userService.findById(userId).toFuture().get();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        if (!(user == null)) {
            return user;
        } else log.info(MessageFormat.format("User with ID {0} not found", userId));
        return null;
    }

    public Set<User> observersSet(Set<String> observerId) {
        Set<User> observers = new HashSet<>();
        observerId.forEach(obsId -> observers.add(findUser(obsId)));
        return observers;
    }


    public Mono<Task> zipLocAuthor(Task taskMono) {
        return Mono.zip(taskRepository.findById(taskMono.getId()), userService.findById(taskMono.getAuthorId()),
                (task, user) -> Task.builder()
                        .id(task.getId())
                        .name(task.getName())
                        .description(task.getDescription())
                        .createdAt(task.getCreatedAt())
                        .updatedAt(task.getUpdatedAt())
                        .status(task.getStatus())
                        .authorId(task.getAuthorId())
                        .assigneeId(task.getAssigneeId())
                        .observerIds(task.getObserverIds())
                        .author(user)
                        .build()
        );
    }

    public Mono<Task> zipLocAssignee(Task taskMono) {
        return Mono.zip(taskRepository.findById(taskMono.getId()), userService.findById(taskMono.getAssigneeId()),
                (task, user) -> Task.builder()
                        .id(taskMono.getId())
                        .name(taskMono.getName())
                        .description(taskMono.getDescription())
                        .createdAt(taskMono.getCreatedAt())
                        .updatedAt(taskMono.getUpdatedAt())
                        .status(taskMono.getStatus())
                        .authorId(taskMono.getAuthorId())
                        .assigneeId(taskMono.getAssigneeId())
                        .author(taskMono.getAuthor())
                        .assignee(user)
                        .observerIds(taskMono.getObserverIds())
                        .build()
        );
    }

    Mono<Task> zipLocObservers(Task taskForSet, String id, Set<User> userSetS) {
        return Mono.zip(taskRepository.findById(taskForSet.getId()), userService.findById(id),
                (task, user) ->
                        Task.builder()
                                .id(taskForSet.getId())
                                .name(taskForSet.getName())
                                .description(taskForSet.getDescription())
                                .createdAt(taskForSet.getCreatedAt())
                                .updatedAt(taskForSet.getUpdatedAt())
                                .status(taskForSet.getStatus())
                                .authorId(taskForSet.getAuthorId())
                                .assigneeId(taskForSet.getAssigneeId())
                                .observerIds(taskForSet.getObserverIds())
                                .author(taskForSet.getAuthor())
                                .assignee(taskForSet.getAssignee())
                                .observers(userSetAdd(userSetS, user))
                                .build()
        );
    }

    public Mono<Task> zipLocSet(Task taskForSet) {
        Mono<Task> userMono = Mono.never();
        final Set<User> userSetS = new HashSet<>();
        for (String id : taskForSet.getObserverIds()) {
            userMono = zipLocObservers(taskForSet, id, userSetS);
        }
        return userMono;
    }


    public static Set<User> userSetAdd(Set<User> userSet, User user) {
        log.info("-- userSetAdd is calling--");
        userSet.add(user);
        return userSet;
    }


}










