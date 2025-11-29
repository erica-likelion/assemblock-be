package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Proposal_target")
public class ProposalTarget {

    @EmbeddedId
    private ProposalTargetId id;

    @ManyToOne
    @MapsId("proposalId")
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;

    @ManyToOne
    @MapsId("proposalBlockId")  
    @JoinColumn(name = "proposalblock_id")
    private Block block;


    @ManyToOne
    @JoinColumn(name = "proposer_id")
    private User proposer;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_status", nullable = false)
    private Status responseStatus = Status.pending;
}
