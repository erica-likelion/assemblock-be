package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.UserProfileType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BlockResponse {

    // Block 정보
    private Long blockId;
    private String blockTitle;
    private Block.BlockCategory categoryName;
    private Block.BlockType blockType;
    private Integer contributionScore;
    private String toolsText;
    private String oneLineSummary;
    private Block.TechPart techPart; // 단일 기술 파트 Enum

    private Long writerId;
    private String writerNickname;
    private UserProfileType writerProfileType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BlockResponse(Block block) {
        this.blockId = block.getId();
        this.blockTitle = block.getBlockTitle();
        this.categoryName = block.getCategoryName();
        this.blockType = block.getBlockType();
        this.contributionScore = block.getContributionScore();
        this.toolsText = block.getToolsText();
        this.oneLineSummary = block.getOneLineSummary();

        this.techPart = block.getTechPart();

        if (block.getUser() != null) {
            this.writerId = block.getUser().getId();
            this.writerNickname = block.getUser().getNickname();
            this.writerProfileType = block.getUser().getProfileType();
        }

        this.createdAt = block.getCreatedAt();
        this.updatedAt = block.getUpdatedAt();
    }
}