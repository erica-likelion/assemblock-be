// 프로젝트 코드 구현 후 확인 필요

package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.Project;
import com.assemblock.assemblock_be.Entity.ProjectMember;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Optional<ProjectMember> findByProjectAndUser(Project project, User user);
    List<ProjectMember> findAllByProject(Project project);
    List<ProjectMember> findAllByUser(User user);
}