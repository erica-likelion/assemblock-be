package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Proposal_targets",
        indexes = {
                @Index(name = "idx_proposalblock_id", columnList = "proposalblock_id")
        }
)

@DynamicUpdate
public class ProposalTarget {
    @EmbeddedId
    private ProposalTargetId id;

    @MapsId("proposal")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_ProposalTargets_proposal_id"))
    private Proposal proposal;

    @MapsId("proposalBlock")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposalblock_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_ProposalTargets_proposalblock_id"))
    private Block proposalBlock;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "response_status", nullable = false)
    private ProposalStatus responseStatus = ProposalStatus.pending;
}