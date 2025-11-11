package com.assemblock.assemblock_be.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BlockResponseDto {
    private Long blockId;
    private String title;
    private String description;
    private String username;
    private String coverImageUrl;

    @Builder
    public BlockResponseDto(Long blockId, String title, String description, String username, String coverImageUrl) {
        this.blockId = blockId;
        this.title = title;
        this.description = description;
        this.username = username;
        this.coverImageUrl = coverImageUrl;
    }
}