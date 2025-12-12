package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.ProposalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalTargetDto {
    private Long userId;
    private String nickname;
    private String blockTitle;      // 어떤 블록으로 참여했는지
    private ProposalStatus status;  // [핵심] WAITING, ACCEPTED, REFUSED
}