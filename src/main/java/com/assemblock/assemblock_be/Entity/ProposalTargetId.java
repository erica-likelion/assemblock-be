package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProposalTargetId implements Serializable {

    @Column(name = "proposal_id")
    private Long proposalId;

    @Column(name = "proposalblock_id")
    private Long proposalBlockId; 

    public ProposalTargetId() {}

    public ProposalTargetId(Long proposalId, Long proposalBlockId) {
        this.proposalId = proposalId;
        this.proposalBlockId = proposalBlockId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProposalTargetId)) return false;
        ProposalTargetId that = (ProposalTargetId) o;
        return Objects.equals(proposalId, that.proposalId) &&
               Objects.equals(proposalBlockId, that.proposalBlockId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proposalId, proposalBlockId);
    }
}
