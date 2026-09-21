package com.aryan.task_manager.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private String ownerName;
}
