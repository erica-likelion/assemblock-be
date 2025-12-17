package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.ProjectDetailResponseDto;
import com.assemblock.assemblock_be.Entity.User;
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

     // 단일 프로젝트 상세 조회
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponseDto> getProjectDetail(
            @PathVariable Long projectId
    ) {
        ProjectDetailResponseDto response = projectService.getProjectDetail(projectId);
        return ResponseEntity.ok(response);
    }

    // 진행 중인 프로젝트 목록 조회 (팀원 리스트 포함)
    @GetMapping("/ongoing")
    public ResponseEntity<List<ProjectDetailResponseDto>> getOngoingProjects(
            @AuthenticationPrincipal User user
    ) {
        List<ProjectDetailResponseDto> response = projectService.getMyOngoingProjects(user.getId());
        return ResponseEntity.ok(response);
    }

    // 프로젝트 완료 처리 (팀장만 가능)
    @PatchMapping("/{projectId}/complete")
    public ResponseEntity<Void> completeProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User user
    ) {
        projectService.completeProject(projectId, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/complete")
    public ResponseEntity<List<ProjectDetailResponseDto>> getCompletedProjects(
            @AuthenticationPrincipal User user
    ) {
        List<ProjectDetailResponseDto> response = projectService.getMyCompletedProjects(user.getId());
        return ResponseEntity.ok(response);
    }
}