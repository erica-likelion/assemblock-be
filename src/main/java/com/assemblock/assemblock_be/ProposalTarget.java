package com.assemblock.assemblock_be.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Proposal_target")
public class ProposalTarget {
    @EmbeddedId
    private com.assemblock.assemblock_be.Entity.ProposalTargetId id;

    @JsonIgnore
    @MapsId("proposalId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id")
    private com.assemblock.assemblock_be.Entity.Proposal proposal;

    @JsonIgnore
    @MapsId("proposalBlockId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposalblock_id")
    private Block block;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposer_id", nullable = false)
    private User proposer;

    @Setter
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "response_status", nullable = false)
    private Status responseStatus = Status.pending;
}