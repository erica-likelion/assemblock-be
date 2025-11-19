package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.SearchDto;
import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.BlockCategoryName;
import com.assemblock.assemblock_be.Entity.TechPart.TechName;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {
    private final BlockRepository blockRepository;

    public List<BlockResponseDto> searchBlocks(SearchDto searchDto) {
        String keyword = searchDto.getKeyword();
        String categoryName = searchDto.getCategoryName();
        String techPart = searchDto.getTechPart();

        if ((keyword == null || keyword.isBlank()) && (categoryName == null || categoryName.isBlank()) && (techPart == null || techPart.isBlank())) {
            return Collections.emptyList();
        }

        BlockCategoryName categoryEnum = null;
        if (categoryName != null && !categoryName.isBlank()) {
            try {
                categoryEnum = BlockCategoryName.valueOf(categoryName.toUpperCase());
            } catch (IllegalArgumentException e) {
            }
        }

        TechName techEnum = null;
        if (techPart != null && !techPart.isBlank()) {
            try {
                techEnum = TechName.valueOf(techPart.toUpperCase());
            } catch (IllegalArgumentException e) {
            }
        }

        List<Block> blocks = blockRepository.findBySearchCriteria(keyword, categoryEnum, techEnum);

        return blocks.stream()
                .map(this::blockToResponseDto)
                .collect(Collectors.toList());
    }

    private BlockResponseDto blockToResponseDto(Block block) {
        User user = block.getUser();

        return BlockResponseDto.builder()
                .blockId(block.getId())
                .blockTitle(block.getTitle())
                .onelineSummary(block.getOnelineSummary())
                .userId(user.getId())
                .categoryName(block.getCategoryName() != null ? block.getCategoryName().getDbValue() : null)
                .blockType(block.getBlockType().name())
                .contributionScore(block.getContributionScore().intValue())
                .resultUrl(block.getResultUrl())
                .toolsText(block.getToolsText())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileImageUrl())
                .build();
    }
}