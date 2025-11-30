package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.ProposalTargetUpdateRequestDto;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.ProposalTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProposalTargetService {

    private final ProposalTargetRepository proposalTargetRepository;

    @Transactional
    public void updateResponseStatus(Long proposalId, Long blockId, ProposalTargetUpdateRequestDto dto) {

        ProposalTargetId id = new ProposalTargetId(proposalId, blockId);

        ProposalTarget target = proposalTargetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProposalTarget not found. proposalId=" + proposalId + ", blockId=" + blockId));

        try {
            ProposalStatus newStatus = ProposalStatus.valueOf(dto.getStatus().toLowerCase());
            target.setResponseStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 상태값입니다: " + dto.getStatus());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("상태값이 비어있습니다.");
        }

        proposalTargetRepository.save(target);
    }
}