package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/me")
    public ResponseEntity<List<Project>> getMyProjects(
            @AuthenticationPrincipal User user
    ) {
        Long currentUserId = user.getId();
        List<Project> projects = projectService.getMyProjects(currentUserId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/ongoing")
    public ResponseEntity<List<String>> getOngoingProjects(
            @AuthenticationPrincipal User user
    ) {
        Long currentUserId = user.getId();
        List<String> ongoingProjects = projectService.getOngoingProjects(userId);
        return ResponseEntity.ok(ongoingProjects);
    }

    @PatchMapping("/{projectId}/complete")
    public ResponseEntity<Void> completeProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User user
    ) {
        Long currentUserId = user.getId();
        projectService.completeProject(projectId, currentUserId);
        return ResponseEntity.ok().build();
    }

}