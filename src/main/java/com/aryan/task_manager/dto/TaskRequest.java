package com.aryan.task_manager.dto;

import com.aryan.task_manager.enums.TaskPriority;
import com.aryan.task_manager.enums.TaskStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TaskRequest {

    @NotBlank(message = "Task title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Builder.Default
    private TaskStatus status = TaskStatus.TODO;

    @Builder.Default
    private TaskPriority priority = TaskPriority.MEDIUM;

    private LocalDate dueDate;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private Long assignedToUserId;

}
