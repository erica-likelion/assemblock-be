package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Entity.Project;
import com.assemblock.assemblock_be.Entity.ProjectMember;
import com.assemblock.assemblock_be.Entity.ProjectStatus;
import com.assemblock.assemblock_be.Repository.ProjectRepository;
import com.assemblock.assemblock_be.Repository.ProjectMemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    // 1) 내 프로젝트 조회
    public List<Project> getMyProjects(Long userId) {
        return projectRepository.findProjectsByUserId(userId);
    }

    // 2) 진행중 리스트 조회
    public List<Project> getOngoingProjects() {
        return projectRepository.findByProjectStatus(ProjectStatus.ongoing);
    }

    // 3) 프로젝트 완료 처리
    public void completeProject(Long projectId, Long userId) {
        ProjectMember member = projectMemberRepository
                .findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("팀원이 아닙니다."));

        if (!member.isProposer()) {
            throw new IllegalArgumentException("제안자만 완료 가능");
        }

        Project project = member.getProject();
        project.setProjectStatus(ProjectStatus.done);
        projectRepository.save(project);
    }

    // 4) 완료 리스트 조회
    public List<Project> getCompletedProjects(Long userId) {
        return projectRepository.findProjectsByUserId(userId)
                .stream()
                .filter(p -> p.getProjectStatus() == ProjectStatus.done)
                .toList();
    }
}
