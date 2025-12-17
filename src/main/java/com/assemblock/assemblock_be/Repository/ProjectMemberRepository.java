package com.assemblock.assemblock_be.Repository;

import java.util.Optional;

import com.assemblock.assemblock_be.Entity.Project;
import com.assemblock.assemblock_be.Entity.ProjectMember;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    Optional<ProjectMember> findByProject_IdAndUser_Id(Long projectId, Long userId);

    Optional<ProjectMember> findByProjectAndUser(Project project, User user);
}