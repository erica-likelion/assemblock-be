package com.assemblock.assemblock_be.Repository;

import java.util.List;

import com.assemblock.assemblock_be.Entity.Proposal;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByProposer_UserId(Long userId);
}