package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.Proposal;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    List<Proposal> findAllByUser(User user);

    @Query("SELECT pt.proposal FROM ProposalTarget pt WHERE pt.user = :user")
    List<Proposal> findAllReceivedProposals(@Param("user") User user);
}