package com.aryan.task_manager.dto;

import com.aryan.task_manager.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusRequest {

    @NotNull(message = "Status is required")
    private TaskStatus status;
}