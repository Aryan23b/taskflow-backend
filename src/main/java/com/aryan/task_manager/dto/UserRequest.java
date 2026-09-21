package com.aryan.task_manager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserRequest {

    @NotBlank(message = "Name is not blank")
    private String name;

    @NotBlank(message ="Email is not blank")
    @Email(message = "Enter Valid email")
    private String email;


}
