package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.ProjectMemberCreateRequestDto;
import com.assemblock.assemblock_be.Dto.ProjectMemberResponseDto;
import com.assemblock.assemblock_be.Entity.*;
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

    // 0. 프로젝트 생성 (제안 -> 프로젝트)
    @PostMapping
    public ResponseEntity<Void> createProject(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, Object> request
    ) {
        Long proposalId = Long.valueOf(request.get("proposalId").toString());
        String memberRole = (String) request.get("memberRole");

        projectService.createProject(user.getUser(), proposalId, memberRole);
        return ResponseEntity.ok().build();
    }

    // 1. 내 프로젝트 조회
    @GetMapping("/me")
    public ResponseEntity<List<Project>> getMyProjects(
            @AuthenticationPrincipal User user
    ) {
        Long currentUserId = user.getUser();
        List<Project> projects = projectService.getMyProjects(currentUserId);
        return ResponseEntity.ok(projects);
    }

    // 2. 진행 중인 프로젝트 조회
    @GetMapping("/ongoing")
    public ResponseEntity<List<String>> getOngoingProjects(
            @AuthenticationPrincipal User user
    ) {
        Long currentUserId = user.getUser();
        List<String> ongoingProjects = projectService.getOngoingProjects(currentUserId);
        return ResponseEntity.ok(ongoingProjects);
    }

    // 3. 프로젝트 완료 처리
    @PatchMapping("/{projectId}/complete")
    public ResponseEntity<Void> completeProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User user
    ) {
        Long currentUserId = user.getUser();
        projectService.completeProject(projectId, currentUserId);
        return ResponseEntity.ok().build();
    }

    // 4. 프로젝트 멤버 추가
    @PostMapping("/members")
    public ResponseEntity<Void> addProjectMember(
            @RequestBody ProjectMemberCreateRequestDto requestDto
    ) {
        projectService.addProjectMember(requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMemberResponseDto>> getProjectMembers(
            @PathVariable Long projectId
    ) {
        List<ProjectMemberResponseDto> members = projectService.getProjectMembers(projectId);
        return ResponseEntity.ok(members);
    }
}