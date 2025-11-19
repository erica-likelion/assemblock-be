package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Projects",
        indexes = {
                @Index(name = "idx_proposer_id_project", columnList = "proposer_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_proposal_id", columnNames = {"proposal_id"})
        }
)
@DynamicUpdate
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "FK_Projects_proposal_id"))
    private Proposal proposal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposer_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_Projects_proposer_id"))
    private User proposer;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status", nullable = false)
    private ProjectStatus projectStatus = ProjectStatus.recruiting;

    @Column(name = "project_recruit", nullable = false)
    private Integer projectRecruit;

    @Column(name = "project_accpeted", nullable = false)
    private Integer projectAccepted = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}