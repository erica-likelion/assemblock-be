package com.assemblock.assemblock_be.Repository;

import java.util.List;

import com.assemblock.assemblock_be.Entity.Project;
import com.assemblock.assemblock_be.Entity.ProjectStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT pm.project FROM ProjectMember pm WHERE pm.user.userId = :userId")
    List<Project> findProjectsByUserId(Long userId);

    List<Project> findByProjectStatus(ProjectStatus status);
}


