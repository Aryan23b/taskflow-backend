package com.aryan.task_manager.service;


import com.aryan.task_manager.dto.PageResponse;
import com.aryan.task_manager.dto.TaskRequest;
import com.aryan.task_manager.dto.TaskResponse;
import com.aryan.task_manager.entity.Project;
import com.aryan.task_manager.entity.Task;
import com.aryan.task_manager.entity.User;
import com.aryan.task_manager.enums.TaskPriority;
import com.aryan.task_manager.enums.TaskStatus;
import com.aryan.task_manager.repository.ProjectRepository;
import com.aryan.task_manager.repository.TaskRepository;
import com.aryan.task_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor

public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskResponse createTask(TaskRequest request) {

        Project project = projectRepository
                .findById(request.getProjectId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Project not found with id: "
                                        + request.getProjectId()
                        ));

        User assignedUser = null;

        if (request.getAssignedToUserId() != null) {

            assignedUser = userRepository
                    .findById(request.getAssignedToUserId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User not found with id: "
                                            + request.getAssignedToUserId()
                            ));
        }

        Task task = Task.builder()
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .status(
                        request.getStatus() != null
                                ? request.getStatus()
                                : TaskStatus.TODO
                )
                .priority(
                        request.getPriority() != null
                                ? request.getPriority()
                                : TaskPriority.MEDIUM
                )
                .dueDate(request.getDueDate())
                .project(project)
                .assignedTo(assignedUser)
                .build();

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);

    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Task not found with id: " + id
                            ));

            return toResponse(task);
        }

        @Transactional
        public TaskResponse updateTask(
                Long id,
                TaskRequest request) {

            Task task = taskRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Task not found with id: " + id
                            ));

            Project project = projectRepository
                    .findById(request.getProjectId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Project not found with id: "
                                            + request.getProjectId()
                            ));

            User assignedUser = null;

            if (request.getAssignedToUserId() != null) {

                assignedUser = userRepository
                        .findById(request.getAssignedToUserId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found with id: "
                                                + request.getAssignedToUserId()
                                ));
            }

            task.setTitle(request.getTitle().trim());
            task.setDescription(request.getDescription());
            task.setStatus(
                    request.getStatus() != null
                            ? request.getStatus()
                            : task.getStatus()
            );

            task.setPriority(
                    request.getPriority() != null
                            ? request.getPriority()
                            : task.getPriority()
            );
            task.setDueDate(request.getDueDate());
            task.setProject(project);
            task.setAssignedTo(assignedUser);

            Task updatedTask =taskRepository.save(task);

            return toResponse(updatedTask);
        }

        @Transactional
        public void deleteTask(Long id) {

            Task task = taskRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Task not found with id: " + id
                            ));

            // this only deletes completed task  and for now we delete any kind of task
//            if (task.getStatus() != TaskStatus.COMPLETED) {
//
//                throw new IllegalStateException(
//                        "Only completed tasks can be deleted"
//                );
//            }


            taskRepository.delete(task);
        }

        @Transactional(readOnly = true)
        public PageResponse<TaskResponse> searchTasks(
                TaskStatus status,
                TaskPriority priority,
                Long projectId,
                Long assignedUserId,
                String title,
                int page,
                int size,
                String sortBy,
                String direction) {

            if (page < 0) {
                throw new IllegalArgumentException(
                        "Page cannot be negative"
                );
            }

            if (size < 1 || size > 50) {
                throw new IllegalArgumentException(
                        "Size must be between 1 and 50"
                );
            }

            Set<String> allowedSortFields = Set.of(
                    "id",
                    "title",
                    "status",
                    "priority",
                    "dueDate"
            );

            if (!allowedSortFields.contains(sortBy)) {
                throw new IllegalArgumentException(
                        "Invalid sort field: " + sortBy
                );
            }

            Sort sort;

            if ("desc".equalsIgnoreCase(direction)) {
                sort = Sort.by(sortBy).descending();
            } else if ("asc".equalsIgnoreCase(direction)) {
                sort = Sort.by(sortBy).ascending();
            } else {
                throw new IllegalArgumentException(
                        "Direction must be either asc or desc"
                );
            }

            Pageable pageable =
                    PageRequest.of(page, size, sort);

            Page<Task> taskPage =
                    taskRepository.searchTasks(
                            status,
                            priority,
                            projectId,
                            assignedUserId,
                            normalizeTitle(title),
                            pageable
                    );

            Page<TaskResponse> responsePage =
                    taskPage.map(this::toResponse);

            return PageResponse.<TaskResponse>builder()
                    .content(responsePage.getContent())
                    .page(responsePage.getNumber())
                    .size(responsePage.getSize())
                    .totalElements(
                            responsePage.getTotalElements()
                    )
                    .totalPages(
                            responsePage.getTotalPages()
                    )
                    .first(responsePage.isFirst())
                    .last(responsePage.isLast())
                    .build();
        }

        private String normalizeTitle(String title) {

            if (title == null || title.isBlank()) {
                return null;
            }

            return title.trim();
        }

        private TaskResponse toResponse(Task task) {

            Long assignedUserId = null;
            String assignedUserName = null;

            if (task.getAssignedTo() != null) {
                assignedUserId =
                        task.getAssignedTo().getId();

                assignedUserName =
                        task.getAssignedTo().getName();
            }

            return TaskResponse.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .description(task.getDescription())
                    .status(task.getStatus())
                    .priority(task.getPriority())
                    .dueDate(task.getDueDate())
                    .projectId(task.getProject().getId())
                    .projectName(task.getProject().getName())
                    .assignedToUserId(assignedUserId)
                    .assignedToUserName(assignedUserName)
                    .build();
        }
}
