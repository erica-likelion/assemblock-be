package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.UserProfileType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BlockResponseDto {

    private Long blockId;
    private String blockTitle;
    private Block.BlockCategory categoryName;
    private Block.BlockType blockType;
    private Integer contributionScore;
    private String toolsText;
    private String oneLineSummary;
    private Block.TechPart techPart;
    private String improvementPoint;
    private String resultUrl;
    private String resultFile;


    private Long writerId;
    private String writerNickname;
    private UserProfileType writerProfileType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BlockResponseDto(Block block) {
        this.blockId = block.getId();
        this.blockTitle = block.getBlockTitle();
        this.categoryName = block.getCategoryName();
        this.blockType = block.getBlockType();
        this.contributionScore = block.getContributionScore();
        this.toolsText = block.getToolsText();
        this.oneLineSummary = block.getOneLineSummary();
        this.techPart = block.getTechPart();
        this.improvementPoint = block.getImprovementPoint();
        this.resultUrl = block.getResultUrl();
        this.resultFile = block.getResultFile();

        if (block.getUser() != null) {
            this.writerId = block.getUser().getUser();
            this.writerNickname = block.getUser().getNickname();
            this.writerProfileType = block.getUser().getProfileType();
        }

        this.createdAt = block.getCreatedAt();
        this.updatedAt = block.getUpdatedAt();
    }

    public static BlockResponseDto fromEntity(Block block) {
        return new BlockResponseDto(block);
    }
}