package com.assemblock.assemblock_be.dto.proposal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalResponseDto {

    private Long proposalId;
    private Long proposerId;
    private String discordId;
    private LocalDate recruitStartDate;
    private LocalDate recruitEndDate;
    private String projectTitle;
    private String projectMemo;
    private LocalDateTime createdAt;
}
