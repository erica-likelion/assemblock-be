package com.assemblock.assemblock_be.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectMemberInfoResponseDto {
    private Long userId;
    private String nickname;
    private String profileUrl; // 유저 프로필 이미지
    private boolean isLeader;  // 팀장 여부 (isProposer)
    private String part;       // 맡은 파트 (MemberRole)
}