package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Entity.Project;
import com.assemblock.assemblock_be.Entity.ProjectMember;
import com.assemblock.assemblock_be.Repository.ProjectMemberRepository;
import com.assemblock.assemblock_be.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectMember create(ProjectMember pm) {
        return projectMemberRepository.save(pm);
    }

    @Transactional
    public void delete(Long id) {
        projectMemberRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ProjectMember findById(Long id) {
        return projectMemberRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ProjectMember> findAll() {
        return projectMemberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ProjectMember> getTeamMembers(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트 없음"));

        return projectMemberRepository.findAllByProject(project);
    }
}