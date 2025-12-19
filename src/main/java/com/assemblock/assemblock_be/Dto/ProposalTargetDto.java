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
    private String blockTitle;
    private ProposalStatus status;  // WAITING, ACCEPTED, REFUSED
}