package com.assemblock.assemblock_be.dto.project;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberResponseDto {

    private Long memberId;
    private Long projectId;
    private Long userId;
    private String memberRole;
    private boolean isProposer;
}
