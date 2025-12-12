package com.assemblock.assemblock_be.Dto;

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

    private String discordId;
    private LocalDate recruitStartDate;
    private LocalDate recruitEndDate;
    private String projectTitle;
    private String projectMemo;
}