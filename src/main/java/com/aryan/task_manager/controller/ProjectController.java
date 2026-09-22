package com.aryan.task_manager.controller;


import com.aryan.task_manager.dto.ProjectRequest;
import com.aryan.task_manager.dto.ProjectResponse;
import com.aryan.task_manager.dto.TaskResponse;
import com.aryan.task_manager.service.ProjectService;
import com.aryan.task_manager.service.TaskService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")

public class ProjectController {
    private final ProjectService projectService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createPoject(
            @Valid
            @RequestBody ProjectRequest request)
    {
        ProjectResponse response=projectService.createProject(request);

        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects(){
        return ResponseEntity.ok(
                projectService.getAllProjects()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse>
    getProjectById(@PathVariable Long id){
        return  ResponseEntity.ok(projectService.getProjectById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id) {

        projectService.deleteProject(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskResponse>> getProjectTasks(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                taskService.getTasksByProject(id)
        );
    }
}
