package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.NotificationResponseDto;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ProposalRepository proposalRepository;

    public List<NotificationResponseDto> getPendingNotifications(Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Block> myBlocks = blockRepository.findAllByUser(user);
        if (myBlocks.isEmpty()) {
            return Collections.emptyList();
        }

        List<ProposalTarget> targets = proposalTargetRepository.findAllByBlockInAndResponseStatus(myBlocks, ProposalStatus.PENDING);

        return targets.stream()
                .map(ProposalTarget::getProposal)
                .distinct()
                .map(proposal -> {
                    User sender = proposal.getUser();
                    long blockCount = proposalTargetRepository.countByProposal(proposal);

                    String content = proposal.getProjectTitle() + " (" + blockCount + "개 블록 제안)";

                    return NotificationResponseDto.builder()
                            .proposalId(proposal.getId())
                            .senderName(sender.getNickname())
                            .senderProfileType(sender.getProfileType())
                            .content(content)
                            .build();
                })
                .collect(Collectors.toList());
    }
}