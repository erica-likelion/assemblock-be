package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.NotificationResponseDto;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.BlockRepository;
import com.assemblock.assemblock_be.Repository.ProposalTargetRepository;
import com.assemblock.assemblock_be.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final ProposalTargetRepository proposalTargetRepository;

    public List<NotificationResponseDto> getPendingNotifications(Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Block> myBlocks = blockRepository.findAllByUser(user);
        if (myBlocks.isEmpty()) {
            return Collections.emptyList();
        }

        List<ProposalTarget> targets = proposalTargetRepository.findAllByProposalBlockInAndResponseStatus(myBlocks, ProposalStatus.pending);

        return targets.stream()
                .map(ProposalTarget::getProposal)
                .distinct()
                .map(proposal -> {
                    User sender = proposal.getProposer();
                    long blockCount = proposalTargetRepository.countByProposal(proposal);
                    String content = proposal.getProjectTitle() + " (" + blockCount + "개 블록 제안)";

                    return NotificationResponseDto.builder()
                            .proposalId(proposal.getId())
                            .senderName(sender.getNickname())
                            .senderProfileType(sender.getProfileType()) // DTO 필드 변경 반영
                            .content(content)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void acceptProposal(Long currentUserId, Long proposalId) throws AccessDeniedException {
        updateProposalTargetsStatus(currentUserId, proposalId, ProposalStatus.accepted);
    }

    @Transactional
    public void rejectProposal(Long currentUserId, Long proposalId) throws AccessDeniedException {
        updateProposalTargetsStatus(currentUserId, proposalId, ProposalStatus.rejected);
    }

    private void updateProposalTargetsStatus(Long currentUserId, Long proposalId, ProposalStatus newStatus)
            throws AccessDeniedException {

        List<ProposalTarget> targets = proposalTargetRepository.findAllByProposalId(proposalId);

        if (targets.isEmpty()) {
            throw new IllegalArgumentException("제안을 찾을 수 없습니다.");
        }

        for (ProposalTarget target : targets) {
            Long blockOwnerId = target.getProposalBlock().getUser().getId();
            if (!blockOwnerId.equals(currentUserId)) {
                throw new AccessDeniedException("이 제안을 처리할 권한이 없습니다.");
            }
            if (target.getResponseStatus() != ProposalStatus.pending) {
                throw new IllegalStateException("이미 처리된 제안이 포함되어 있습니다.");
            }
        }

        for (ProposalTarget target : targets) {
            target.setResponseStatus(newStatus);
        }
    }
}