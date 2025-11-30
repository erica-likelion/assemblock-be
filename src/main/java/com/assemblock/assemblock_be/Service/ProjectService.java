package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.ProjectMemberCreateRequestDto;
import com.assemblock.assemblock_be.Dto.ProjectMemberResponseDto;
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

    // 3) 프로젝트 완료 처리
    @Transactional
    public void completeProject(Long projectId, Long userId) {
        ProjectMember member = projectMemberRepository
                .findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("팀원이 아닙니다."));

        if (!member.getIsProposer()) {
            throw new IllegalArgumentException("제안자만 완료 가능");
        }

        Project project = member.getProject();
        project.setProjectStatus(ProjectStatus.done);
        projectRepository.save(project);
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
        List<ProjectMember> members = projectMemberRepository.findByProject_ProjectId(projectId);

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
}