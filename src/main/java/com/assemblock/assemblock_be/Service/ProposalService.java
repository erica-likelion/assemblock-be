package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.*;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 1. 제안 생성 + 프로젝트 자동 생성 + 제안자 멤버 등록
    @Transactional
    public ProposalResponseDto createProposal(Long userId, ProposalCreateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 1-1. 제안서 생성
        Proposal proposal = Proposal.builder()
                .user(user)
                .discordId(dto.getDiscordId())
                .recruitStartDate(dto.getRecruitStartDate())
                .recruitEndDate(dto.getRecruitEndDate())
                .projectTitle(dto.getProjectTitle())
                .projectMemo(dto.getProjectMemo())
                .build();
        Proposal savedProposal = proposalRepository.save(proposal);

        // 1-2. [자동] 프로젝트 생성 (상태: recruiting)
        // Project 생성자가 (Proposal, User)를 받는다고 가정 (이전 코드 기반)
        Project project = new Project(savedProposal, user);
        Project savedProject = projectRepository.save(project);

        // 1-3. [자동] 제안자를 '팀장(PM)'으로 멤버 등록
        // [수정] MemberRole.LEADER -> MemberRole.PM (Enum에 존재하는 값 사용)
        ProjectMember proposerMember = new ProjectMember(
                savedProject,
                user,
                savedProposal,
                user,
                MemberRole.PM,
                true // isProposer = true
        );
        projectMemberRepository.save(proposerMember);

        return getProposalDetail(savedProposal.getId());
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

    // 3. 제안 응답 + 멤버 자동 추가 + 프로젝트 자동 시작
    @Transactional
    public ProposalResponseDto respondToProposal(Long userId, Long proposalId, ProposalTargetUpdateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("제안서를 찾을 수 없습니다."));

        ProposalTarget target = proposalTargetRepository.findByProposalAndUser(proposal, user)
                .orElseThrow(() -> new IllegalArgumentException("당신은 이 제안의 대상자가 아닙니다."));

        // 3-1. 상태 업데이트 (ProposalTarget 엔티티에 updateStatus 메서드 필요)
        target.setResponseStatus(request.getResponseStatus());

        // 3-2. [자동] 수락(ACCEPTED) 시 프로젝트 멤버로 추가
        if (request.getResponseStatus() == ProposalStatus.ACCEPTED) {
            Project project = projectRepository.findByProposal_Id(proposalId)
                    .orElseThrow(() -> new IllegalStateException("연관된 프로젝트가 없습니다."));

            // [수정] MemberRole.MEMBER는 존재하지 않으므로, 블록 정보를 기반으로 역할을 결정해야 합니다.
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

        // 3-3. [자동] 모든 대상자가 응답했는지 확인 -> 프로젝트 시작
        checkAndStartProject(proposal);

        // 변경된 정보 반환
        return getProposalDetail(proposalId);
    }

    // [Helper] 블록 정보를 기반으로 MemberRole 결정
    private MemberRole determineRoleFromBlock(Block block) {
        // Block 엔티티에 'techPart'나 'category' 같은 필드가 있다고 가정하고 매핑합니다.
        // 만약 매핑이 실패하면 기본값(Plan 등)을 주거나 에러를 낼 수 있습니다.
        try {
            // 예: block.getTechPart()가 "FRONTEND"라면 MemberRole.FrontEnd로 변환
            // 대소문자 무시하고 매핑 시도
            // String roleStr = block.getTechPart(); // Block에 해당 필드가 있다면 사용
            // return MemberRole.valueOf(roleStr);

            // 임시: 일단 기본값으로 Plan 설정 (실제 로직에 맞게 수정 필요)
            return MemberRole.Plan;
        } catch (Exception e) {
            return MemberRole.Plan; // 매핑 실패 시 기본값
        }
    }

    // [Helper] 모든 팀원이 응답했는지 확인 후 프로젝트 상태 변경
    private void checkAndStartProject(Proposal proposal) {
        List<ProposalTarget> allTargets = proposalTargetRepository.findAllByProposal_Id(proposal.getId());

        // 아무도 PENDING 상태가 아니면 (모두 응답했으면)
        boolean allResponded = allTargets.stream()
                .noneMatch(t -> t.getResponseStatus() == ProposalStatus.PENDING);

        if (allResponded) {
            Project project = projectRepository.findByProposal_Id(proposal.getId())
                    .orElseThrow(() -> new IllegalStateException("프로젝트 없음"));

            // 프로젝트 상태를 'ongoing'(진행중)으로 변경
            project.setProjectStatus(ProjectStatus.ongoing);
            projectRepository.save(project);
        }
    }

    // 4. 내 제안 목록 조회
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