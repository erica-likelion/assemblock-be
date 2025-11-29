package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.CategoryResponseDto;
import com.assemblock.assemblock_be.Dto.SubCategoryDto;
import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.BlockCategoryName;
import com.assemblock.assemblock_be.Entity.TechPart;
import com.assemblock.assemblock_be.Repository.BlockRepository;
import com.assemblock.assemblock_be.Repository.TechPartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final TechPartRepository techPartRepository;
    private final BlockRepository blockRepository;

    public List<CategoryResponseDto> getCategoriesWithTools() {
        List<TechPart> allCategories = techPartRepository.findAll();
        return allCategories.stream().map(category -> {
            List<Block> blocksInThisCategory = blockRepository.findByTechParts(category);
            List<SubCategoryDto> subCategories = blocksInThisCategory.stream()
                    .map(Block::getCategoryName)
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(enumValue -> new SubCategoryDto(enumValue.name()))
                    .collect(Collectors.toList());

            return new CategoryResponseDto(category, subCategories);
        }).collect(Collectors.toList());
    }

    public List<BlockResponseDto> getBlocksByCategoryName(String categoryName) {
        BlockCategoryName categoryEnum;
        try {
            categoryEnum = BlockCategoryName.valueOf(categoryName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 카테고리 이름입니다: " + categoryName);
        }

        List<Block> blocks = blockRepository.findByCategoryName(categoryEnum);
        return blocks.stream()
                .map(block -> BlockResponseDto.builder()
                        .blockId(block.getId())
                        .title(block.getTitle())
                        .description(block.getOnelineSummary())
                        .username(block.getUser().getNickname())
                        .coverImageUrl(block.getResultUrl())
                        .build())
                .collect(Collectors.toList());
    }
}