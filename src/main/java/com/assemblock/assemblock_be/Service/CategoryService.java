package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.CategoryResponseDto;
import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.BlockCategoryName;
import com.assemblock.assemblock_be.Entity.BlockType;
import com.assemblock.assemblock_be.Repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final BlockRepository blockRepository;

    public List<CategoryResponseDto> getCategoriesWithTools() {
        return Arrays.stream(Block.TechPart.values()).map(techPart -> {
            List<Block> blocksInThisPart = blockRepository.findAllByTechPart(techPart);

            List<String> subCategories = blocksInThisPart.stream()
                    .map(Block::getCategoryName)
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(BlockCategoryName::getDbValue)
                    .collect(Collectors.toList());

            return new CategoryResponseDto(techPart, subCategories);
        }).collect(Collectors.toList());
    }

    public List<BlockResponseDto> getBlocks(String categoryName, String type) {
        List<Block> blocks;

        if (categoryName != null && !categoryName.isBlank()) {
            BlockCategoryName categoryEnum = Arrays.stream(BlockCategoryName.values())
                    .filter(c -> c.name().equals(categoryName) || c.getDbValue().equals(categoryName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 이름입니다: " + categoryName));

            blocks = blockRepository.findAllByCategoryNameOrderByCreatedAtDesc(categoryEnum);

        } else if (type != null && !type.isBlank()) {
            try {
                BlockType blockType = BlockType.valueOf(type.toUpperCase());
                blocks = blockRepository.findAllByBlockTypeOrderByCreatedAtDesc(blockType);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 블록 타입입니다: " + type);
            }
        } else {
            throw new IllegalArgumentException("카테고리 이름 또는 블록 타입 중 하나는 필수입니다.");
        }

        return blocks.stream()
                .map(BlockResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}