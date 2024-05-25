package com.example.TasktrackerSecurity.repository;


import com.example.TasktrackerSecurity.entity.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {

    Mono<Task> findByName(String name);

}
