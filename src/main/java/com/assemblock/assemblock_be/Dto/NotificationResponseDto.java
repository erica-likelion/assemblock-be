package com.assemblock.assemblock_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Long proposalId;
    private String senderName;
    private ProfileType senderProfileType;
    private String content;
}