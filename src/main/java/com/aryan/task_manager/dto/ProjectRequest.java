package com.aryan.task_manager.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProjectRequest {
    @NotBlank(message = "Project name is blank")
    private String name;

    private String description;

    @NotNull(message = "Owner id is Required")
    private Long ownerId;
}
