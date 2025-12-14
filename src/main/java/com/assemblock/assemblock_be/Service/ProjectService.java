package com.assemblock.assemblock_be.Service;

// import com.assemblock.assemblock_be.Dto.ProjectMemberCreateRequestDto;
// import com.assemblock.assemblock_be.Dto.ProjectMemberResponseDto;
import com.assemblock.assemblock_be.Dto.ProjectDetailResponseDto;
import com.assemblock.assemblock_be.Dto.ProjectMemberInfoResponseDto;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;

    @Transactional
    public void createProject(Long userId, Long proposalId, String roleStr) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new IllegalArgumentException("제안을 찾을 수 없습니다."));

        if (!proposal.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("제안자만 프로젝트를 생성할 수 있습니다.");
        }

        if (projectRepository.findByProposal_Id(proposalId).isPresent()) {
            throw new IllegalArgumentException("이미 생성된 프로젝트입니다.");
        }

        MemberRole memberRole;
        try {
            memberRole = MemberRole.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("유효하지 않은 역할입니다: " + roleStr);
        }

        Project project = new Project(proposal, proposal.getUser());
        projectRepository.save(project);

        ProjectMember member = new ProjectMember(
                project,
                proposal.getUser(),
                proposal,
                proposal.getUser(),
                memberRole,
                true
        );
        projectMemberRepository.save(member);
    }

    public ProjectDetailResponseDto getProjectDetail(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        // 멤버 리스트를 DTO로 변환
        List<ProjectMemberInfoResponseDto> memberDtos = projectMemberRepository.findByProject_Id(projectId).stream()
                .map(member -> ProjectMemberInfoResponseDto.builder()
                        .userId(member.getUser().getId())
                        .nickname(member.getUser().getNickname())
                        .profileUrl(member.getUser().getPortfolioUrl()) // User 엔티티에 필드가 있다고 가정
                        .isLeader(member.getIsProposer())
                        .part(member.getMemberRole().name()) // Enum -> String
                        .build())
                .collect(Collectors.toList());

        return ProjectDetailResponseDto.builder()
                .projectId(project.getId())
                .projectTitle(project.getProposal().getProjectTitle()) // Proposal의 제목 사용
                .status(project.getProjectStatus())
                .members(memberDtos)
                .build();
    }

    /**
     * 진행 중인 프로젝트 목록 보기
     * - 내가 속한 프로젝트 중 status가 'ongoing'인 것들
     * - 각 프로젝트의 팀원 리스트 포함
     */
    public List<ProjectDetailResponseDto> getMyOngoingProjects(Long userId) {
        List<Project> myProjects = projectRepository.findProjectsByUserId(userId);

        return myProjects.stream()
                .filter(project -> project.getProjectStatus() == ProjectStatus.ongoing ||
                        project.getProjectStatus() == ProjectStatus.recruiting)
                .map(project -> getProjectDetail(project.getId())) // 위에서 만든 상세 조회 메서드 재사용
                .collect(Collectors.toList());
    }

    /**
     * 프로젝트 완료 처리
     * - 프로젝트 팀장(Proposer)만 가능
     * - status를 'done'으로 변경
     */
    @Transactional
    public void completeProject(Long projectId, Long userId) {
        ProjectMember member = projectMemberRepository.findByProject_IdAndUser_Id(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트의 멤버가 아닙니다."));

        if (!member.getIsProposer()) {
            throw new IllegalArgumentException("프로젝트 팀장만 완료 처리를 할 수 있습니다.");
        }

        Project project = member.getProject();
        project.setProjectStatus(ProjectStatus.done);

        projectRepository.save(project);
    }
/*
    // 1) 내 프로젝트 조회
    public List<Project> getMyProjects(Long userId) {
        return projectRepository.findProjectsByUserId(userId);
    }

    // 2) 진행중 리스트 조회
    public List<String> getOngoingProjects(Long userId) {
        return projectRepository.findProjectsByUserId(userId)
                .stream()
                .filter(project -> project.getProjectStatus() == ProjectStatus.ongoing)
                .map(project -> project.getProposal().getProjectTitle())
                .collect(Collectors.toList());
    }

    // 4) 멤버 추가
    @Transactional
    public void addProjectMember(ProjectMemberCreateRequestDto dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        MemberRole role = MemberRole.valueOf(dto.getMemberRole());

        ProjectMember member = new ProjectMember(
                project,
                user,
                project.getProposal(),
                project.getProposer(),
                role,
                dto.isProposer()
        );

        projectMemberRepository.save(member);
    }

    // 5) 프로젝트 멤버 목록 조회
    public List<ProjectMemberResponseDto> getProjectMembers(Long projectId) {
        List<ProjectMember> members = projectMemberRepository.findByProject_Id(projectId);

        return members.stream()
                .map(member -> ProjectMemberResponseDto.builder()
                        .memberId(member.getId())
                        .projectId(member.getProject().getId())
                        .userId(member.getUser().getId())
                        .memberRole(member.getMemberRole().name())
                        .isProposer(member.getIsProposer())
                        .build())
                .collect(Collectors.toList());
    }

 */

    public List<ProjectDetailResponseDto> getMyCompletedProjects(Long userId) {
        List<Project> myProjects = projectRepository.findProjectsByUserId(userId);

        return myProjects.stream()
                .filter(project -> project.getProjectStatus() == ProjectStatus.done) // 완료된 것만 필터링
                .map(project -> getProjectDetail(project.getId())) // 상세 조회 메서드 재사용
                .collect(Collectors.toList());
    }
}