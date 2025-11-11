package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Proposal_targets")
@IdClass(ProposalTargetId.class)
public class ProposalTarget {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id", nullable = false)
    private Proposal proposal;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposalblock_id", nullable = false)
    private Block proposalBlock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposer_id", nullable = false)
    private User proposer;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "response_status", nullable = false)
    private ProposalStatus responseStatus = ProposalStatus.pending;
}