package com.aryan.task_manager.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="projects")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(
            name="owner_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_project_owner"
            )
    )
    private User owner;
}
