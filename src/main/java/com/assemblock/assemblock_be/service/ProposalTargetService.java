package com.assemblock.assemblock_be.service;

import com.assemblock.assemblock_be.entity.ProposalTarget;
import com.assemblock.assemblock_be.entity.ProposalTargetId;
import com.assemblock.assemblock_be.entity.Status;
import com.assemblock.assemblock_be.repository.ProposalTargetRepository;
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

        // Status enum: accepted / rejected / pending
        Status newStatus = Status.valueOf(status.toLowerCase()); // 만약 enum이 소문자라면
        target.setResponseStatus(newStatus);

        return proposalTargetRepository.save(target);
    }
}
