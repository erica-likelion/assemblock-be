// 제안 코드 구현 후 확인 필요

package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.Proposal;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findAllByProposer(User proposer);
}