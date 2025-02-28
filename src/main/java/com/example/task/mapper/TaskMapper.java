package com.example.task.mapper;

import com.example.task.model.Task;
import com.example.task.dto.TaskModel;
import com.example.task.dto.TaskModelCreate;
import com.example.task.dto.TaskModelUpdate;
import java.util.concurrent.ExecutionException;

public interface TaskMapper {
    TaskModelUpdate taskToTaskModelUpdate(Task task);
    TaskModel taskToTaskModel(Task task);
    Task taskModelCreateToTask(TaskModelCreate modelCreate) throws ExecutionException, InterruptedException;
    Task taskModelUpdateToTask(TaskModelUpdate modelUpdate);
    Task taskModeltoTask(TaskModel taskModel);
}

