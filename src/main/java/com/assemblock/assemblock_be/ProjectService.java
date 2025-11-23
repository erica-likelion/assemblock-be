package com.assemblock.assemblock_be.service;

import com.assemblock.assemblock_be.Entity.Project;
import com.assemblock.assemblock_be.Entity.ProjectMember;
import com.assemblock.assemblock_be.Entity.ProjectStatus;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Repository.ProjectMemberRepository;
import com.assemblock.assemblock_be.Repository.ProjectRepository;
import com.assemblock.assemblock_be.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Project> getMyProjects(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return projectMemberRepository.findAllByUser(user).stream()
                .map(ProjectMember::getProject)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Project> getOngoingProjects(Long userId) {
        return getMyProjects(userId).stream()
                .filter(p -> p.getProjectStatus() == ProjectStatus.recruiting ||
                        p.getProjectStatus() == ProjectStatus.ongoing)
                .collect(Collectors.toList());
    }

    @Transactional
    public void completeProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        ProjectMember member = projectMemberRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new IllegalArgumentException("팀원이 아닙니다."));

        if (!Boolean.TRUE.equals(member.getIsProposer())) {
            throw new IllegalArgumentException("제안자만 완료 가능");
        }

        project.setProjectStatus(ProjectStatus.done);
        projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public List<Project> getCompletedProjects(Long userId) {
        return getMyProjects(userId).stream()
                .filter(p -> p.getProjectStatus() == ProjectStatus.done)
                .collect(Collectors.toList());
    }
}