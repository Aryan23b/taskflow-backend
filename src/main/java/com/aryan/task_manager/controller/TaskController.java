package com.aryan.task_manager.controller;


import com.aryan.task_manager.dto.PageResponse;
import com.aryan.task_manager.dto.TaskRequest;
import com.aryan.task_manager.dto.TaskResponse;
import com.aryan.task_manager.dto.TaskStatusRequest;
import com.aryan.task_manager.enums.TaskPriority;
import com.aryan.task_manager.enums.TaskStatus;
import com.aryan.task_manager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor

public class TaskController {

    private final TaskService taskService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponse<TaskResponse>> searchTasks(

            @RequestParam(required = false)
            TaskStatus status,

            @RequestParam(required = false)
            TaskPriority priority,

            @RequestParam(required = false)
            Long projectId,

            @RequestParam(required = false)
            Long assignedUserId,

            @RequestParam(required = false)
            String title,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "dueDate")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction
    ) {

        return ResponseEntity.ok(
                taskService.searchTasks(
                        status,
                        priority,
                        projectId,
                        assignedUserId,
                        title,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }


    // =========================================================
    // GET MY ASSIGNED TASKS
    // ADMIN + USER
    // =========================================================

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/my")
    public ResponseEntity<List<TaskResponse>> getMyTasks(
            Authentication authentication
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                taskService.getTasksForCurrentUser(email)
        );
    }


    // =========================================================
    // GET TASK BY ID
    // ADMIN ONLY
    // =========================================================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                taskService.getTaskById(id)
        );
    }


    // =========================================================
    // CREATE TASK
    // ADMIN ONLY
    // =========================================================

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request
    ) {

        TaskResponse response =
                taskService.createTask(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // UPDATE TASK
    // ADMIN ONLY
    // =========================================================

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request
    ) {

        return ResponseEntity.ok(
                taskService.updateTask(id, request)
        );
    }


    // =========================================================
    // DELETE TASK
    // ADMIN ONLY
    // =========================================================

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id
    ) {

        taskService.deleteTask(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateMyTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody TaskStatusRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                taskService.updateMyTaskStatus(
                        id,
                        request.getStatus(),
                        email
                )
        );
    }
}