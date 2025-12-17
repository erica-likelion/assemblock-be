package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.ProjectStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ProjectDetailResponseDto {
    private Long projectId;
    private String projectTitle;
    private ProjectStatus status;
    private LocalDate recruitStartDate;
    private LocalDate recruitEndDate;
    private String contact;
    private List<ProjectMemberInfoResponseDto> members;
}