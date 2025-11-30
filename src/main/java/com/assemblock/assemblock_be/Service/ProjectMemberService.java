package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Entity.ProjectMember;
import com.assemblock.assemblock_be.Repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;

    public ProjectMember create(ProjectMember pm) {
        return projectMemberRepository.save(pm);
    }

    public void delete(Long id) {
        projectMemberRepository.deleteById(id);
    }

    public ProjectMember findById(Long id) {
        return projectMemberRepository.findById(id)
                .orElse(null);
    }

    public List<ProjectMember> findAll() {
        return projectMemberRepository.findAll();
    }

    public List<ProjectMember> getTeamMembers(Long projectId) {
        return projectMemberRepository.findByProject_ProjectId(projectId);
    }
}