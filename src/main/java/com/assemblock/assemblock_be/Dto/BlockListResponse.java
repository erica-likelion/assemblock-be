package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Block;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BlockListResponse {

    private Long blockId;
    private String blockTitle;
    private Block.BlockCategory categoryName;
    private Block.BlockType blockType;
    private Block.TechPart techPart;
    private Integer contributionScore;
    private String oneLineSummary;

    private Long writerId;
    private String writerNickname;

    private LocalDateTime createdAt;

    public BlockListResponse(Block block) {
        this.blockId = block.getId();
        this.blockTitle = block.getBlockTitle();
        this.categoryName = block.getCategoryName();
        this.blockType = block.getBlockType();
        this.techPart = block.getTechPart();
        this.contributionScore = block.getContributionScore();
        this.oneLineSummary = block.getOneLineSummary();
        this.createdAt = block.getCreatedAt();

        if (block.getUser() != null) {
            this.writerId = block.getUser().getId();
            this.writerNickname = block.getUser().getNickname();
        }
    }
}