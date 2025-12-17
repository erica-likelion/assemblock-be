package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.*;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final UserRepository userRepository;
    private final ProposalTargetRepository proposalTargetRepository;

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final BoardRepository boardRepository;

    // [필수 추가] 이 줄이 없어서 에러가 났습니다.
    private final BlockRepository blockRepository;

    // 1. 제안 생성 + 타겟 블록 저장 + 프로젝트 자동 생성 + 제안자 멤버 등록
    @Transactional
    public ProposalResponseDto createProposal(Long userId, ProposalCreateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("보드를 찾을 수 없습니다."));

        // 제안서 생성
        Proposal proposal = Proposal.builder()
                .user(user)
                .discordId(dto.getDiscordId())
                .recruitStartDate(dto.getRecruitStartDate())
                .recruitEndDate(dto.getRecruitEndDate())
                .projectTitle(dto.getProjectTitle())
                .projectMemo(dto.getProjectMemo())
                .build();
        Proposal savedProposal = proposalRepository.save(proposal);

        List<BoardBlock> boardBlocks = board.getBoardBlocks();

        if (boardBlocks != null && !boardBlocks.isEmpty()) {
            for (BoardBlock boardBlock : boardBlocks) {
                Block block = boardBlock.getBlock();


                ProposalTarget target = ProposalTarget.builder()
                        .proposal(savedProposal)
                        .block(block)
                        .user(block.getUser())
                        .responseStatus(ProposalStatus.PENDING)
                        .build();
                proposalTargetRepository.save(target);
            }
        } else {
            throw new IllegalArgumentException("보드에 블록이 하나도 없습니다.");
        }

        Project project = new Project(savedProposal, user);
        Project savedProject = projectRepository.save(project);

        // 제안자를 '팀장(PM)'으로 멤버 자동 등록
        ProjectMember proposerMember = new ProjectMember(
                savedProject,
                user,
                savedProposal,
                user,
                MemberRole.PM,
                true // isProposer = true
        );
        projectMemberRepository.save(proposerMember);

        // 저장된 제안서의 상세 정보를 반환 (타겟 정보 포함)
        return getProposalDetail(savedProposal.getId());
    }

    @Transactional
    public void autoRefuseOverdueTargets() {
        LocalDate today = LocalDate.now();

        // 1. 마감일이 어제까지였는데(오늘 기준 마감일 < 오늘) 아직 PENDING인 타겟들 조회
        List<ProposalTarget> overdueTargets = proposalTargetRepository
                .findAllByProposal_RecruitEndDateBeforeAndResponseStatus(today, ProposalStatus.PENDING);

        if (overdueTargets.isEmpty()) {
            return;
        }

        for (ProposalTarget target : overdueTargets) {
            // 2. 상태를 REFUSED로 변경
            target.setResponseStatus(ProposalStatus.REJECTED);

            checkAndStartProject(target.getProposal());
        }
    }

    // 2. 제안 상세 조회
    public ProposalResponseDto getProposalDetail(Long id) {
        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("제안을 찾을 수 없습니다."));

        List<ProposalTargetDto> targetDtos = proposal.getTargets().stream()
                .map(target -> ProposalTargetDto.builder()
                        .userId(target.getUser().getId())
                        .nickname(target.getUser().getNickname())
                        .blockTitle(target.getBlock().getBlockTitle())
                        .status(target.getResponseStatus())
                        .build())
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
                .targets(targetDtos)
                .build();
    }

    @Transactional
    public ProposalResponseDto respondToProposal(Long userId, Long proposalId, ProposalTargetUpdateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("제안서를 찾을 수 없습니다."));

        ProposalTarget target = proposalTargetRepository.findByProposalAndUser(proposal, user)
                .orElseThrow(() -> new IllegalArgumentException("당신은 이 제안의 대상자가 아닙니다."));

        target.setResponseStatus(request.getResponseStatus());

        if (request.getResponseStatus() == ProposalStatus.ACCEPTED) {
            Project project = projectRepository.findByProposal_Id(proposalId)
                    .orElseThrow(() -> new IllegalStateException("연관된 프로젝트가 없습니다."));

            MemberRole role = determineRoleFromBlock(target.getBlock());

            ProjectMember newMember = new ProjectMember(
                    project,
                    user,
                    proposal,
                    proposal.getUser(),
                    role,
                    false // isProposer = false (팀원)
            );
            projectMemberRepository.save(newMember);
        }

        checkAndStartProject(proposal);

        return getProposalDetail(proposalId);
    }

    private MemberRole determineRoleFromBlock(Block block) {
        try {
            return MemberRole.Plan; // 임시 기본값
        } catch (Exception e) {
            return MemberRole.Plan;
        }
    }

    private void checkAndStartProject(Proposal proposal) {
        List<ProposalTarget> allTargets = proposalTargetRepository.findAllByProposal_Id(proposal.getId());

        // 아무도 PENDING 상태가 아니면
        boolean allResponded = allTargets.stream()
                .noneMatch(t -> t.getResponseStatus() == ProposalStatus.PENDING);

        if (allResponded) {
            Project project = projectRepository.findByProposal_Id(proposal.getId())
                    .orElseThrow(() -> new IllegalStateException("프로젝트 없음"));

            // 프로젝트 상태를 ongoing
            project.setProjectStatus(ProjectStatus.ongoing);
            projectRepository.save(project);
        }
    }

    public List<ProposalListDto> getMyProposals(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Proposal> sentProposals = proposalRepository.findAllByUser(user);
        List<Proposal> receivedProposals = proposalRepository.findAllReceivedProposals(user);

        return Stream.concat(sentProposals.stream(), receivedProposals.stream())
                .distinct()
                .map(proposal -> convertToListDto(proposal, userId))
                .sorted(Comparator.comparing(ProposalListDto::getProposalId).reversed())
                .collect(Collectors.toList());
    }

    private ProposalListDto convertToListDto(Proposal proposal, Long currentUserId) {
        boolean isSender = proposal.getUser().getId().equals(currentUserId);
        String type = isSender ? "보낸 제안" : "받은 제안";

        List<String> teamNames = new ArrayList<>();
        teamNames.add(proposal.getUser().getNickname());

        if (proposal.getTargets() != null) {
            List<String> targetNames = proposal.getTargets().stream()
                    .map(target -> target.getUser().getNickname())
                    .collect(Collectors.toList());
            teamNames.addAll(targetNames);
        }

        return ProposalListDto.builder()
                .proposalId(proposal.getId())
                .type(type)
                .teamMemberNames(teamNames)
                .build();
    }

    @Transactional
    public void delete(Long id) {
        proposalRepository.deleteById(id);
    }
}