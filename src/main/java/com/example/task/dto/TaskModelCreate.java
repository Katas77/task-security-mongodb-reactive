package com.example.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskModelCreate {
    private String name;
    private String description;
    private String authorId;
    private String assigneeId;
    private Set<String> observerIds;
}
