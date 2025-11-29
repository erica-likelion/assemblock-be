package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.BlockType;
import com.assemblock.assemblock_be.Entity.ProfileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockResponseDto {
    private Long blockId;
    private String title;
    private String onelineSummary;
    private String nickname;
    private ProfileType profileType;
    private BlockType blockType;
    private String categoryName;
    private Block.TechPart techPart;

    public static BlockResponseDto fromEntity(Block block) {
        return BlockResponseDto.builder()
                .blockId(block.getId())
                .title(block.getTitle())
                .onelineSummary(block.getOnelineSummary())
                .nickname(block.getUser().getNickname())
                .profileType(block.getUser().getProfileType())
                .blockType(block.getBlockType())
                .categoryName(block.getCategoryName() != null ? block.getCategoryName().getDbValue() : null)
                .techPart(block.getTechPart())
                .build();
    }
}