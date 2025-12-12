package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "proposal_target")
public class ProposalTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proposal_target_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id")
    private Proposal proposal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id")
    private Block block;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_status")
    @Builder.Default
    private ProposalStatus responseStatus = ProposalStatus.PENDING;

    public void setResponseStatus(ProposalStatus status) {
        this.responseStatus = status;
    }
}