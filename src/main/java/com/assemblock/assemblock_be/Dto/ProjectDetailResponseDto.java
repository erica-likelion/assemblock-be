package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.ProjectStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProjectDetailResponseDto {
    private Long projectId;
    private String projectTitle;
    private ProjectStatus status; // recruiting, ongoing, done
    private List<ProjectMemberInfoResponseDto> members; // 팀원 목록
}