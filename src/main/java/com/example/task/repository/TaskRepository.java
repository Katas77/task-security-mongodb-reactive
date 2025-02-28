package com.example.task.repository;

import com.example.task.model.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {


}
