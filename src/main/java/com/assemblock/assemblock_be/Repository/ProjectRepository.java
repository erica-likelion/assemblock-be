package com.assemblock.assemblock_be.Repository;

import java.util.List;

import com.assemblock.assemblock_be.Entity.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT pm.project FROM ProjectMember pm WHERE pm.user.id = :userId")
    List<Project> findProjectsByUserId(@Param("userId") Long userId);
}