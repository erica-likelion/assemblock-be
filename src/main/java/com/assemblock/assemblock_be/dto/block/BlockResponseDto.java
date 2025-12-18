package com.assemblock.assemblock_be.dto.block;

import com.assemblock.assemblock_be.entity.Block;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BlockResponseDto {

    private Long blockId;
    private String blockTitle;
    private String blockType;
    private String resultFileName;

    public static BlockResponseDto from(Block block) {
        return BlockResponseDto.builder()
                .blockId(block.getBlockId())
                .blockTitle(block.getBlockTitle())
                .blockType(block.getBlockType())
                .resultFileName(block.getResultFileName())
                .build();
    }
}
