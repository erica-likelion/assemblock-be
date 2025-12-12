package com.assemblock.assemblock_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProposalListDto {

    private Long proposalId;
    private String type;  // 보낸 제안 or 받은 제안
    private List<String> teamMemberNames; // 팀원 전체 이름

}
