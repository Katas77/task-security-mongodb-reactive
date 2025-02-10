package com.example.task.mapper;


import com.example.task.model.Task;
import com.example.task.dto.TaskModel;
import com.example.task.dto.TaskModelCreate;
import com.example.task.dto.TaskModelUpdate;
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

