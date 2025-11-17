package com.assemblock.assemblock_be.service;

import com.assemblock.assemblock_be.entity.ProjectMember;
import com.assemblock.assemblock_be.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectMemberRepository repository;

    public ProjectMember create(ProjectMember pm) {
        return repository.save(pm);
    }

    public ProjectMember findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<ProjectMember> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // 내 팀 보기
    public List<ProjectMember> getTeamMembers(Long projectId) {
        return projectMemberRepository.findByProject_ProjectId(projectId);
    }

}
