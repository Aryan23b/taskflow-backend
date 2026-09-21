package com.aryan.task_manager.controller;


import com.aryan.task_manager.dto.ProjectRequest;
import com.aryan.task_manager.dto.ProjectResponse;
import com.aryan.task_manager.service.ProjectService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor

public class ProjectController {
    private final ProjectService projectService;

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
}
