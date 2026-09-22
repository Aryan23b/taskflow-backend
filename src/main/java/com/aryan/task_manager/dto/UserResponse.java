package com.aryan.task_manager.dto;


import com.aryan.task_manager.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
