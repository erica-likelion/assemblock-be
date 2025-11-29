package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Entity.Project;
import com.assemblock.assemblock_be.Service.ProjectService;
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

    @GetMapping("/ongoing")
    public List<Project> getOngoingProjects(@RequestParam Long userId) {
        return projectService.getOngoingProjects(userId);
    }

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
