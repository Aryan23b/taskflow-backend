package com.aryan.task_manager.controller;


import com.aryan.task_manager.dto.PageResponse;
import com.aryan.task_manager.dto.TaskRequest;
import com.aryan.task_manager.dto.TaskResponse;
import com.aryan.task_manager.enums.TaskPriority;
import com.aryan.task_manager.enums.TaskStatus;
import com.aryan.task_manager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor

public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request) {

        TaskResponse response =
                taskService.createTask(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                taskService.getTaskById(id)
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<TaskResponse>>
    searchTasks(

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
            String direction) {

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

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {

        return ResponseEntity.ok(
                taskService.updateTask(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }

}
