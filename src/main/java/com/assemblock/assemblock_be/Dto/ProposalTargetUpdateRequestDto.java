package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.ProposalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProposalTargetUpdateRequestDto {
    private ProposalStatus responseStatus;
}