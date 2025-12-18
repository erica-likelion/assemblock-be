package com.assemblock.assemblock_be.service;

import com.assemblock.assemblock_be.dto.block.BlockCreateRequestDto;
import com.assemblock.assemblock_be.dto.block.BlockResponseDto;
import com.assemblock.assemblock_be.entity.Block;
import com.assemblock.assemblock_be.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;

    public BlockResponseDto create(BlockCreateRequestDto dto) {
        Block block = new Block();
        block.setBlockTitle(dto.getBlockTitle());
        block.setBlockType(dto.getBlockType());
        block.setContributionScore(dto.getContributionScore());
        block.setOnelineSummary(dto.getOnelineSummary());
        block.setResultUrl(dto.getResultUrl());
        block.setResultFile(dto.getResultFile());
        block.setResultFileName(dto.getResultFileName()); 

        Block saved = blockRepository.save(block);

        return BlockResponseDto.from(saved);
    }

    public BlockResponseDto findById(Long blockId) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));

        return BlockResponseDto.from(block);
    }
}
