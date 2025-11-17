package com.assemblock.assemblock_be.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "proposals")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proposal_id")
    private Long proposalId;

    @ManyToOne
    @JoinColumn(name = "proposer_id", nullable = false)
    private User proposer;

    @Column(name = "discord_id", nullable = false)
    private String discordId;

    @Column(name = "recruit_start_date", nullable = false)
    private LocalDate recruitStartDate;

    @Column(name = "recruit_end_date", nullable = false)
    private LocalDate recruitEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "recruit_status", nullable = false)
    private Status recruitStatus = Status.pending;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "project_title", nullable = false)
    private String projectTitle;

    @Column(name = "project_memo", nullable = false)
    private String projectMemo;
}
