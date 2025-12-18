package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.ProposalStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectMemberInfoResponseDto {
    private Long userId;
    private String nickname;
    private String profileUrl;
    private boolean isLeader;
    private String part;
    private ProposalStatus status;
}