package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.ProposalCreateRequestDto;
import com.assemblock.assemblock_be.Dto.ProposalResponseDto;
import com.assemblock.assemblock_be.Entity.Proposal;
import com.assemblock.assemblock_be.Entity.ProposalTarget;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Repository.ProposalRepository;
import com.assemblock.assemblock_be.Repository.ProposalTargetRepository;
import com.assemblock.assemblock_be.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProposalService {

    private static final Logger log = LoggerFactory.getLogger(ProposalService.class);

    private final ProposalRepository proposalRepository;
    private final UserRepository userRepository;
    private final ProposalTargetRepository proposalTargetRepository;

    @Transactional
    public void createProposal(ProposalCreateRequestDto dto) {
        User user = userRepository.findById(dto.getProposerId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        log.info("새 Proposal 생성자 유저 ID: {}", user.getId());

        Proposal proposal = Proposal.builder()
                .user(user)
                .discordId(dto.getDiscordId())
                .recruitStartDate(dto.getRecruitStartDate())
                .recruitEndDate(dto.getRecruitEndDate())
                .projectTitle(dto.getProjectTitle())
                .projectMemo(dto.getProjectMemo())
                .build();

        proposalRepository.save(proposal);
    }

    public ProposalResponseDto getProposalDetail(Long id) {
        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("제안을 찾을 수 없습니다."));

        log.info("제안서 상세 조회: {}", proposal.toString());

        List<BlockResponseDto> targetBlocks = proposalTargetRepository.findAllByProposal_Id(id)
                .stream()
                .map(ProposalTarget::getBlock)
                .map(BlockResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ProposalResponseDto.builder()
                .proposalId(proposal.getId())
                .proposerId(proposal.getUser().getId())
                .proposerNickname(proposal.getUser().getNickname())
                .discordId(proposal.getDiscordId())
                .recruitStartDate(proposal.getRecruitStartDate())
                .recruitEndDate(proposal.getRecruitEndDate())
                .projectTitle(proposal.getProjectTitle())
                .projectMemo(proposal.getProjectMemo())
                .createdAt(proposal.getCreatedAt())
                .targetBlocks(targetBlocks)
                .build();
    }

    public List<Proposal> getMyProposals(Long userId) {
        return proposalRepository.findAllByUser_Id(userId);
    }

    public List<Proposal> findAll() {
        return proposalRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        proposalRepository.deleteById(id);
    }
}