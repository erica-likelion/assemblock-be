package com.assemblock.assemblock_be.entity;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "proposal_targets")
public class ProposalTarget {

    @EmbeddedId
    private ProposalTargetId id;

    @ManyToOne
    @MapsId("proposalId")
    @JoinColumn(name = "proposal_id", nullable = false)
    private Proposal proposal;

    @ManyToOne
    @JoinColumn(name = "proposer_id", nullable = false)
    private User proposer;

    @ManyToOne
    @MapsId("proposalBlockId")
    @JoinColumn(name = "proposalblock_id", nullable = false)
    private Block proposalBlock;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_status", nullable = false)
    private Status responseStatus = Status.pending;
}
