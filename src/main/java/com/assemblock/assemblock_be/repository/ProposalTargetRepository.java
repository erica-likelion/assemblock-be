package com.assemblock.assemblock_be.repository;

import com.assemblock.assemblock_be.Entity.ProposalTarget;
import com.assemblock.assemblock_be.Entity.ProposalTargetId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalTargetRepository extends JpaRepository<ProposalTarget, ProposalTargetId> {
}
