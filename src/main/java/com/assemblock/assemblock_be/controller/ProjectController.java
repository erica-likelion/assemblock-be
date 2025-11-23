package com.assemblock.assemblock_be.controller;

import com.assemblock.assemblock_be.entity.Project;
import com.assemblock.assemblock_be.entity.ProjectStatus;
import com.assemblock.assemblock_be.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    // 1) 내가 만든 프로젝트 목록
    @GetMapping("/mine/{userId}")
    public List<Project> getMyProjects(@PathVariable Long userId) {
        return projectService.getMyProjects(userId);
    }

    // 2) 진행중인 프로젝트 목록
    @GetMapping("/ongoing")
    public List<Project> getOngoingProjects() {
        return projectService.getOngoingProjects();
    }

    // 3) 프로젝트 완료 처리
    @PatchMapping("/{projectId}/complete")
    public void completeProject(
            @PathVariable Long projectId,
            @RequestParam Long userId
    ) {
        projectService.completeProject(projectId, userId);
    }

    // 4) 완료된 프로젝트 목록
    @GetMapping("/completed/{userId}")
    public List<Project> getCompletedProjects(@PathVariable Long userId) {
        return projectService.getCompletedProjects(userId);
    }
}
