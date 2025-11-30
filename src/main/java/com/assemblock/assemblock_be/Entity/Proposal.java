package com.assemblock.assemblock_be.Entity;

import com.assemblock.assemblock_be.ProposalTarget;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor()
@AllArgsConstructor
@Builder
@Table(name = "Proposal")
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proposal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposer_id", nullable = false)
    private User proposer;

    @Column(name = "discord_id", nullable = false)
    private String discordId;

    @Column(name = "recruit_start_date", nullable = false)
    private LocalDate recruitStartDate;

    @Column(name = "recruit_end_date", nullable = false)
    private LocalDate recruitEndDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "project_title", nullable = false)
    private String projectTitle;

    @Column(name = "project_memo", nullable = false, columnDefinition = "TEXT")
    private String projectMemo;

    @Builder.Default
    @OneToMany(mappedBy = "proposal", orphanRemoval = true)
    private List<ProposalTarget> targets = new ArrayList<>();
}
