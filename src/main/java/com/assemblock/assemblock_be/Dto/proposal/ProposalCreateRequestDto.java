package com.assemblock.assemblock_be.Dto.proposal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalCreateRequestDto {

    private Long proposerId;          // 제안자 user_id
    private String discordId;         // 디스코드 아이디
    private LocalDate recruitStartDate;   // 모집 시작일
    private LocalDate recruitEndDate;     // 모집 종료일
    private String projectTitle;      // 프로젝트 이름
    private String projectMemo;       // 프로젝트 설명
}
