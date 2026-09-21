package com.aryan.task_manager.repository;

import com.aryan.task_manager.entity.Task;
import com.aryan.task_manager.enums.TaskPriority;
import com.aryan.task_manager.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

public interface TaskRepository
        extends JpaRepository<Task,Long>{

        boolean existsByProjectId(Long projectId);

        @Modifying
        @Query("UPDATE Task t SET t.assignedTo = NULL WHERE t.assignedTo.id = :userId")
        int clearAssignedUser(@Param("userId") Long userId);

    @Query("""
            SELECT t
            FROM Task t
            WHERE (:status IS NULL OR t.status = :status)
              AND (:priority IS NULL OR t.priority = :priority)
              AND (:projectId IS NULL OR t.project.id = :projectId)
              AND (:assignedUserId IS NULL
                   OR t.assignedTo.id = :assignedUserId)
              AND (:title IS NULL
                   OR LOWER(t.title) LIKE LOWER(CONCAT('%', CAST(:title AS string), '%')))
            """)

    Page<Task> searchTasks(
            @Param("status")TaskStatus status,
            @Param("priority")TaskPriority priority,
            @Param("projectId") Long projectId,
            @Param("assignedUserId") Long assignedUserId,
            @Param("title") String title,
            Pageable pageable
            );
}
