package com.aryan.task_manager.controller;


import com.aryan.task_manager.dto.TaskResponse;
import com.aryan.task_manager.dto.UserRequest;
import com.aryan.task_manager.dto.UserResponse;
import com.aryan.task_manager.service.TaskService;
import com.aryan.task_manager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody UserRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskResponse>> getUserTasks(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                taskService.getTasksByAssignedUser(id)
        );
    }



}