package com.example.task.dto;

import com.example.task.model.status.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskModelUpdate {

    private String name;
    private String description;
    private TaskStatus status;
    private String authorId;
    private String assigneeId;
    private Set<String> observerIds;

}
