package com.assemblock.assemblock_be.Entity;

import com.assemblock.assemblock_be.ProjectMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id", nullable = false, unique = true)
    private com.assemblock.assemblock_be.Entity.Proposal proposal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposer_id", nullable = false)
    private User proposer;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status", nullable = false)
    private com.assemblock.assemblock_be.Entity.ProjectStatus projectStatus = com.assemblock.assemblock_be.Entity.ProjectStatus.recruiting;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private List<ProjectMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", orphanRemoval = true)
    private List<com.assemblock.assemblock_be.Entity.Review> reviews = new ArrayList<>();
}