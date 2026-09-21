package com.aryan.task_manager.service;


import com.aryan.task_manager.dto.ProjectRequest;
import com.aryan.task_manager.dto.ProjectResponse;
import com.aryan.task_manager.entity.Project;
import com.aryan.task_manager.entity.User;
import com.aryan.task_manager.repository.ProjectRepository;
import com.aryan.task_manager.repository.TaskRepository;
import com.aryan.task_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ProjectService {

    private final ProjectRepository projectRepository;
        private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProjectResponse createProject(
            ProjectRequest request)
    {
        String name=request.getName().trim();

        if(projectRepository.existsByName(name)){
            throw new RuntimeException(
                    "Project already Exists: "+name
            );
        }

        User owner=userRepository.findById(request.getOwnerId())
                .orElseThrow(()->
                        new RuntimeException(
                                "Owner not Found :"+ request.getOwnerId()
                        ));

        Project project=Project.builder()
                .name(name)
                .description(request.getDescription())
                .owner(owner)
                .build();


        Project savedProject =projectRepository.save(project);

        return toResponse(savedProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {

        return projectRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Project not found with id: " + id
                        ));

        return toResponse(project);
    }

        @Transactional
        public void deleteProject(Long id) {

                Project project = projectRepository.findById(id)
                                .orElseThrow(() ->
                                                new RuntimeException(
                                                                "Project not found with id: " + id
                                                ));

                if (taskRepository.existsByProjectId(id)) {
                        throw new IllegalStateException(
                                        "Cannot delete a project that still has tasks."
                        );
                }

                projectRepository.delete(project);
        }

    private ProjectResponse toResponse(Project project) {

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .ownerId(project.getOwner().getId())
                .ownerName(project.getOwner().getName())
                .build();
    }






}
