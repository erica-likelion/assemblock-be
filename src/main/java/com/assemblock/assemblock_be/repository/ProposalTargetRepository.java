package com.assemblock.assemblock_be.repository;

import com.assemblock.assemblock_be.entity.ProposalTarget;
import com.assemblock.assemblock_be.entity.ProposalTargetId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalTargetRepository extends JpaRepository<ProposalTarget, ProposalTargetId> {
}
