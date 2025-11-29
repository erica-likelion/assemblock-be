package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Proposal")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proposal_id")
    private Long proposalId;

    @ManyToOne
    @JoinColumn(name = "proposer_id", nullable = false)
    private User proposer;

    @Column(name = "discord_id", nullable = false, length = 255)
    private String discordId;

    @Column(name = "recruit_start_date", nullable = false)
    private LocalDate recruitStartDate;

    @Column(name = "recruit_end_date", nullable = false)
    private LocalDate recruitEndDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "project_title", nullable = false)
    private String projectTitle;

    @Column(name = "project_memo", nullable = false, columnDefinition = "TEXT")
    private String projectMemo;
}
