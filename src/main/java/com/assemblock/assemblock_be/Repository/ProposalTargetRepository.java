package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.Proposal;
import com.assemblock.assemblock_be.Entity.ProposalStatus;
import com.assemblock.assemblock_be.Entity.ProposalTarget;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProposalTargetRepository extends JpaRepository<ProposalTarget, Long> {

    List<ProposalTarget> findAllByBlockInAndResponseStatus(List<Block> blocks, ProposalStatus responseStatus);

    List<ProposalTarget> findAllByProposal_Id(Long proposalId);

    Optional<ProposalTarget> findByProposalAndUser(Proposal proposal, User user);

    long countByProposal(Proposal proposal);
}