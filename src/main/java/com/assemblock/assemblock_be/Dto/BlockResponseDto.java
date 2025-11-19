package com.assemblock.assemblock_be.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BlockResponseDto {
    private Long blockId;
    private String blockTitle;
    private String onelineSummary;
    private Long userId;
    private String categoryName;
    private String blockType;
    private Integer contributionScore;
    private String resultUrl;
    private String toolsText;
    private String nickname;
    private String profileUrl;
}