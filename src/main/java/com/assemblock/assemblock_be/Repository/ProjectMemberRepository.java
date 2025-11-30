package com.assemblock.assemblock_be.Repository;

import java.util.List;
import java.util.Optional;

import com.assemblock.assemblock_be.Entity.Project;
import com.assemblock.assemblock_be.Entity.ProjectMember;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    List<ProjectMember> findByProject_ProjectId(Long projectId);

    Optional<ProjectMember> findByProject_ProjectIdAndUser_UserId(Long projectId, Long userId);

    Optional<ProjectMember> findByProjectAndUser(Project project, User user);
}