package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockDto;
import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.BlockListResponseDto;
import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Repository.BlockRepository;
import com.assemblock.assemblock_be.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createBlock(Long userId, BlockDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        validateBlockTypeRequirements(requestDto);

        Block block = Block.builder()
                .user(user)
                .blockTitle(requestDto.getBlockTitle())
                .categoryName(requestDto.getCategoryName())
                .techPart(requestDto.getTechPart())
                .blockType(requestDto.getBlockType())
                .contributionScore(requestDto.getContributionScore())
                .toolsText(requestDto.getToolsText())
                .oneLineSummary(requestDto.getOneLineSummary())
                .improvementPoint(requestDto.getImprovementPoint())
                .resultUrl(requestDto.getResultUrl())
                .resultFile(requestDto.getResultFile())
                .build();

        blockRepository.save(block);

        return block.getId();
    }

    @Transactional(readOnly = true)
    public BlockResponseDto getBlockDetail(Long blockId) {
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));

        return new BlockResponseDto(block);
    }

    @Transactional
    public void updateBlock(Long userId, Long blockId, BlockDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));

        if (!block.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User does not have permission to update this block");
        }

        validateBlockTypeRequirements(requestDto);

        block.update(requestDto);
    }

    @Transactional
    public void deleteBlock(Long userId, Long blockId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));

        if (!block.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User does not have permission to delete this block");
        }

        blockRepository.delete(block);
    }

    @Transactional(readOnly = true)
    public List<BlockListResponseDto> getBlockList(
            Optional<Block.BlockCategory> category,
            Optional<Block.TechPart> techPart,
            String keyword) {

        String finalKeyword = (keyword != null && !keyword.isEmpty()) ? keyword : null;

        List<Block> blocks = blockRepository.findBlocksDynamic(
                category.orElse(null),
                techPart.orElse(null),
                finalKeyword
        );

        return blocks.stream().map(BlockListResponseDto::new).collect(Collectors.toList());
    }

    private void validateBlockTypeRequirements(BlockDto dto) {
        if (dto.getBlockType() == Block.BlockType.TECHNOLOGY) {
            if (dto.getTechPart() == null) {
                throw new IllegalArgumentException("TECHNOLOGY 타입은 기술 파트(techPart)가 필수입니다.");
            }
            if (!StringUtils.hasText(dto.getToolsText())) {
                throw new IllegalArgumentException("TECHNOLOGY 타입은 툴 설명(toolsText)이 필수입니다.");
            }
        }
    }
}