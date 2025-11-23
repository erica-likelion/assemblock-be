package com.assemblock.assemblock_be.service;

import com.assemblock.assemblock_be.Entity.ProposalTarget;
import com.assemblock.assemblock_be.Entity.ProposalTargetId;
import com.assemblock.assemblock_be.Entity.Status;
import com.assemblock.assemblock_be.Repository.ProposalTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProposalTargetService {
    private final ProposalTargetRepository proposalTargetRepository;

    @Transactional
    public ProposalTarget updateResponseStatus(Long proposalId, Long proposalBlockId, String status) {
        ProposalTargetId id = new ProposalTargetId(proposalId, proposalBlockId);

        ProposalTarget target = proposalTargetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProposalTarget not found. proposalId=" + proposalId + ", blockId=" + proposalBlockId));

        try {
            Status newStatus = Status.valueOf(status.toLowerCase());
            target.setResponseStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 상태 값입니다: " + status);
        }

        return proposalTargetRepository.save(target);
    }
}