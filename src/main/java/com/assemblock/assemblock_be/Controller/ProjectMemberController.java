package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Entity.ProjectMember;
import com.assemblock.assemblock_be.Service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project-members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    // 팀원 생성
    @PostMapping
    public ProjectMember create(@RequestBody ProjectMember pm) {
        return projectMemberService.create(pm);
    }

    // 팀원 조회
    @GetMapping("/{id}")
    public ProjectMember findById(@PathVariable Long id) {
        return projectMemberService.findById(id);
    }

    // 팀원 전체 조회
    @GetMapping
    public List<ProjectMember> findAll() {
        return projectMemberService.findAll();
    }

    // 팀원 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectMemberService.delete(id);
    }

    // 특정 프로젝트 팀원 조회
    @GetMapping("/project/{projectId}")
    public List<ProjectMember> getTeamMembers(@PathVariable Long projectId) {
        return projectMemberService.getTeamMembers(projectId);
    }
}
