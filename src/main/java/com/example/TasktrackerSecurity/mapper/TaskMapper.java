package com.example.TasktrackerSecurity.mapper;


import com.example.TasktrackerSecurity.entity.Task;
import com.example.TasktrackerSecurity.model.TaskModel;
import com.example.TasktrackerSecurity.model.TaskModelCreate;
import com.example.TasktrackerSecurity.model.TaskModelUpdate;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.concurrent.ExecutionException;

public interface TaskMapper {
    TaskModelUpdate TaskToTaskModelUpdate(Task task);

    TaskModel taskToTaskModel(Task task);

    Task taskModelCreateToTask(TaskModelCreate modelCreate, Mono<Principal> principal) throws ExecutionException, InterruptedException;

    Task taskModelUpdateToTask(TaskModelUpdate modelUpdate);


    Task taskModeltoTask(TaskModel taskModel);
}

