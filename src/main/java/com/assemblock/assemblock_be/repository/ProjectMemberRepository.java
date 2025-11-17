package com.assemblock.assemblock_be.repository;

import java.util.List;
import java.util.Optional;

import com.assemblock.assemblock_be.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    List<ProjectMember> findByProject_ProjectId(Long projectId);

    Optional<ProjectMember> findByProject_ProjectIdAndUser_UserId(Long projectId, Long userId);

}
