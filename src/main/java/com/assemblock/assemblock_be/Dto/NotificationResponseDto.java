package com.assemblock.assemblock_be.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NotificationResponseDto {
    private Long proposalId;
    private String senderName;
    private String senderProfileUrl;
    private String content;

    @Builder
    public NotificationResponseDto(Long proposalId, String senderName, String senderProfileUrl, String content) {
        this.proposalId = proposalId;
        this.senderName = senderName;
        this.senderProfileUrl = senderProfileUrl;
        this.content = content;
    }
}