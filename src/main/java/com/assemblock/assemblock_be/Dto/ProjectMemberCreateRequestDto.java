package com.assemblock.assemblock_be.Dto;

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
public class ProjectMemberCreateRequestDto {

    private Long projectId;   // 참여할 프로젝트 id
    private Long userId;      // 팀원으로 추가할 유저 id
    private String memberRole; // Plan / Design / PM / FrontEnd / BackEnd
    private boolean isProposer; // 제안자인지 여부
}
