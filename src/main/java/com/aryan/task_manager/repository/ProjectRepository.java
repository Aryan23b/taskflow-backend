package com.aryan.task_manager.repository;

import com.aryan.task_manager.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    boolean existsByName(String name);
    boolean existsByOwnerId(Long ownerId);
}
