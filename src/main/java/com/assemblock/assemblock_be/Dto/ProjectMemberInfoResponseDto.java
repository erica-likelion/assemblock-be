package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.ProposalStatus;
import com.assemblock.assemblock_be.Entity.UserProfileType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectMemberInfoResponseDto {
    private Long userId;
    private String nickname;
    private UserProfileType profileType;
    private boolean isLeader;
    private String part;
    private ProposalStatus status;
}