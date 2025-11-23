package com.assemblock.assemblock_be.controller;

import com.assemblock.assemblock_be.entity.ProjectMember;
import com.assemblock.assemblock_be.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project-members")
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @PostMapping
    public ProjectMember create(@RequestBody ProjectMember pm) {
        return projectMemberService.create(pm);
    }

    @GetMapping("/{id}")
    public ProjectMember findById(@PathVariable Long id) {
        return projectMemberService.findById(id);
    }

    @GetMapping
    public List<ProjectMember> findAll() {
        return projectMemberService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectMemberService.delete(id);
    }

    @GetMapping("/project/{projectId}")
    public List<ProjectMember> getTeamMembers(@PathVariable Long projectId) {
        return projectMemberService.getTeamMembers(projectId);
    }
}