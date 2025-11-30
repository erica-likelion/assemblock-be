package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.Proposal;
import com.assemblock.assemblock_be.Entity.ProposalStatus;
import com.assemblock.assemblock_be.Entity.ProposalTarget;
import com.assemblock.assemblock_be.Entity.ProposalTargetId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalTargetRepository extends JpaRepository<ProposalTarget, ProposalTargetId> {

    List<ProposalTarget> findAllByBlockInAndResponseStatus(List<Block> blocks, ProposalStatus responseStatus);

    long countByProposal(Proposal proposal);

    List<ProposalTarget> findAllByProposal_Id(Long proposalId);
}