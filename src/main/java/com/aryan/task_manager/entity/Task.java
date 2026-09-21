package com.aryan.task_manager.entity;


import com.aryan.task_manager.config.TaskStatusConverter;
import com.aryan.task_manager.enums.TaskPriority;
import com.aryan.task_manager.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

        @Convert(converter = TaskStatusConverter.class)
    @Column(nullable = false)

    @Builder.Default
    private TaskStatus status = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TaskPriority priority = TaskPriority.MEDIUM;

    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "project_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_task_project"
            )
    )

    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "assigned_to_id",
            foreignKey = @ForeignKey(
                    name = "fk_task_assigned_user"
            )
    )
    private User assignedTo;


}
