package com.assemblock.assemblock_be.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "Proposal_target")
public class ProposalTarget {

    @EmbeddedId
    private ProposalTargetId id;

    @JsonIgnore
    @MapsId("proposalId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;

    @JsonIgnore
    @MapsId("proposalBlockId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposalblock_id")
    private Block block;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @Setter
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "response_status", nullable = false)
    private ProposalStatus responseStatus = ProposalStatus.pending;
}