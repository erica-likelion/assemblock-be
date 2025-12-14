package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.ProjectDetailResponseDto;
//import com.assemblock.assemblock_be.Dto.ProjectMemberCreateRequestDto;
//import com.assemblock.assemblock_be.Entity.Project;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
/*
    // 0. 프로젝트 생성 (기존 유지)
    @PostMapping
    public ResponseEntity<Void> createProject(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, Object> request
    ) {
        Long proposalId = Long.valueOf(request.get("proposalId").toString());
        String memberRole = (String) request.get("memberRole");

        projectService.createProject(user.getId(), proposalId, memberRole);
        return ResponseEntity.ok().build();
    }

 */

    /**
     * [기능 1] 단일 프로젝트 상세 조회
     * (상태, 멤버 리스트 포함)
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponseDto> getProjectDetail(
            @PathVariable Long projectId
    ) {
        ProjectDetailResponseDto response = projectService.getProjectDetail(projectId);
        return ResponseEntity.ok(response);
    }

    /**
     * [기능 2] 진행 중인 프로젝트 목록 조회 (팀원 리스트 포함)
     * (기존 getOngoingProjects를 대체하거나 새로 추가)
     */
    @GetMapping("/ongoing")
    public ResponseEntity<List<ProjectDetailResponseDto>> getOngoingProjects(
            @AuthenticationPrincipal User user
    ) {
        List<ProjectDetailResponseDto> response = projectService.getMyOngoingProjects(user.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * [기능 3] 프로젝트 완료 처리 (팀장만 가능)
     */
    @PatchMapping("/{projectId}/complete")
    public ResponseEntity<Void> completeProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User user
    ) {
        projectService.completeProject(projectId, user.getId());
        return ResponseEntity.ok().build();
    }
/*
    // [기타] 내 전체 프로젝트 조회 (기존 유지)
    @GetMapping("/me")
    public ResponseEntity<List<Project>> getMyProjects(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(projectService.getMyProjects(user.getId()));
    }

 */

    @GetMapping("/complete")
    public ResponseEntity<List<ProjectDetailResponseDto>> getCompletedProjects(
            @AuthenticationPrincipal User user
    ) {
        List<ProjectDetailResponseDto> response = projectService.getMyCompletedProjects(user.getId());
        return ResponseEntity.ok(response);
    }
/*
    @PostMapping("/members")
    public ResponseEntity<Void> addProjectMember(
            @RequestBody ProjectMemberCreateRequestDto requestDto
    ) {
        projectService.addProjectMember(requestDto);
        return ResponseEntity.ok().build();
    }

 */
}