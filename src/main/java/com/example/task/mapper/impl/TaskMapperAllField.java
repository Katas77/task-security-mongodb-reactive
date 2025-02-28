package com.example.task.mapper.impl;

import com.example.task.model.Task;
import com.example.task.model.status.TaskStatus;
import com.example.task.mapper.TaskMapper;
import com.example.task.mapper.UserMapper;
import com.example.task.dto.TaskModel;
import com.example.task.dto.TaskModelCreate;
import com.example.task.dto.TaskModelUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapperAllField implements TaskMapper {
    private final UserMapper userMapper;

    @Override
    public TaskModelUpdate taskToTaskModelUpdate(Task task) {
        if (task == null) {
            return null;
        }

        return TaskModelUpdate.builder()
                .name(task.getName())
                .description(task.getDescription())
                .status(task.getStatus())
                .authorId(task.getAuthorId())
                .assigneeId(task.getAssigneeId())
                .observerIds(task.getObserverIds())
                .build();
    }

    @Override
    public TaskModel taskToTaskModel(Task task) {
        if (task == null) {
            return null;
        }

        return TaskModel.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .status(task.getStatus())
                .authorId(task.getAuthorId())
                .assigneeId(task.getAssigneeId())
                .observerIds(task.getObserverIds())
                .author(userMapper.userToModel(task.getAuthor()))
                .assignee(userMapper.userToModel(task.getAssignee()))
                .observers(userMapper.setUserModels(task.getObservers()))
                .build();
    }

    @Override
    public Task taskModelCreateToTask(TaskModelCreate modelCreate) {
        if (modelCreate == null) {
            return null;
        }

        return Task.builder()
                .name(modelCreate.getName())
                .description(modelCreate.getDescription())
                .status(TaskStatus.DONE)
                .authorId(modelCreate.getAuthorId())
                .assigneeId(modelCreate.getAssigneeId())
                .observerIds(modelCreate.getObserverIds())
                .build();
    }

    @Override
    public Task taskModelUpdateToTask(TaskModelUpdate modelUpdate) {
        if (modelUpdate == null) {
            return null;
        }

        return Task.builder()
                .name(modelUpdate.getName())
                .description(modelUpdate.getDescription())
                .status(modelUpdate.getStatus())
                .authorId(modelUpdate.getAuthorId())
                .assigneeId(modelUpdate.getAssigneeId())
                .observerIds(modelUpdate.getObserverIds())
                .build();
    }

    @Override
    public Task taskModeltoTask(TaskModel model) {
        if (model == null) {
            return null;
        }

        return Task.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .status(model.getStatus())
                .authorId(model.getAuthorId())
                .assigneeId(model.getAssigneeId())
                .observerIds(model.getObserverIds())
                .author(userMapper.modelToUser(model.getAuthor()))
                .assignee(userMapper.modelToUser(model.getAssignee()))
                .observers(userMapper.setUsers(model.getObservers()))
                .build();
    }
}
