package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.ProjectDetailResponseDto;
import com.assemblock.assemblock_be.Dto.ProjectMemberInfoResponseDto;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;


    public ProjectDetailResponseDto getProjectDetail(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        Proposal proposal = project.getProposal(); // 프로젝트와 연결된 제안서

        List<ProjectMemberInfoResponseDto> memberDtos = new ArrayList<>();

        User leader = proposal.getUser();
        memberDtos.add(ProjectMemberInfoResponseDto.builder()
                .userId(leader.getId())
                .nickname(leader.getNickname())
                .profileType(leader.getProfileType())
                .isLeader(true)
                .part("PM") // 혹은 leader.getProfileType() 등
                .status(ProposalStatus.ACCEPTED) // 팀장은 항상 수락 상태
                .build());

        if (proposal.getTargets() != null) {
            for (ProposalTarget target : proposal.getTargets()) {

                String partName = "Member";
                // if (target.getBlock() != null) partName = target.getBlock().getCategoryName();

                memberDtos.add(ProjectMemberInfoResponseDto.builder()
                        .userId(target.getUser().getId())
                        .nickname(target.getUser().getNickname())
                        .profileType(target.getUser().getProfileType())
                        .isLeader(false)
                        .part(partName)
                        .status(target.getResponseStatus())
                        .build());
            }
        }

        return ProjectDetailResponseDto.builder()
                .projectId(project.getId())
                .projectTitle(proposal.getProjectTitle())
                .status(project.getProjectStatus()) // recruiting, ongoing, done

                .recruitStartDate(proposal.getRecruitStartDate())
                .recruitEndDate(proposal.getRecruitEndDate())
                .contact(proposal.getDiscordId())

                .members(memberDtos) // 팀장 + 팀원 리스트
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
                .map(project -> getProjectDetail(project.getId()))
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

    public List<ProjectDetailResponseDto> getMyCompletedProjects(Long userId) {
        List<Project> myProjects = projectRepository.findProjectsByUserId(userId);

        return myProjects.stream()
                .filter(project -> project.getProjectStatus() == ProjectStatus.done) // 완료된 것만 필터링
                .map(project -> getProjectDetail(project.getId()))
                .collect(Collectors.toList());
    }
}