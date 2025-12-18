package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.UserProfileType;
import com.assemblock.assemblock_be.Entity.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalResponseDto {
    private Long proposalId;
    private Long proposerId;
    private String proposerNickname;
    private UserProfileType proposerProfileType;
    private String proposerRole;
    private String discordId;
    private LocalDate recruitStartDate;
    private LocalDate recruitEndDate;
    private String projectTitle;
    private String projectMemo;
    private LocalDateTime createdAt;
    private List<BlockResponseDto> targetBlocks;

    private List<ProposalTargetDto> targets;
}