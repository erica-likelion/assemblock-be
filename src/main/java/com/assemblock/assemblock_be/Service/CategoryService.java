package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.*;
import com.assemblock.assemblock_be.Entity.*;
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
                    .map(Block::getCategory)
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(BlockCategory::getDbValue)
                    .collect(Collectors.toList());

            return new CategoryResponseDto(techPart, subCategories);
        }).collect(Collectors.toList());
    }

    public List<BlockResponse> getBlocks(String category, String type) {
        List<Block> blocks;

        if (category != null && !category.isBlank()) {
            Block.BlockCategory categoryEnum = Arrays.stream(BlockCategory.values())
                    .filter(c -> c.name().equals(category) || c.getDbValue().equals(category))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 이름입니다: " + category));

            blocks = blockRepository.findAllByCategoryOrderByCreatedAtDesc(categoryEnum);

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
                .map(BlockResponse::fromEntity)
                .collect(Collectors.toList());
    }
}