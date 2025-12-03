package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockDto;
import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.BlockListResponseDto;
import com.assemblock.assemblock_be.Dto.BlockPagingResponseDto;
import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Repository.BlockRepository;
import com.assemblock.assemblock_be.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createBlock(Long userId, BlockDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (blockRepository.findByUserAndBlockTitle(user, requestDto.getBlockTitle()).isPresent()) {
            throw new IllegalArgumentException("이미 동일한 제목의 블록이 존재합니다.");
        }

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

        if (!block.getBlockTitle().equals(requestDto.getBlockTitle())) {
            if (blockRepository.findByUserAndBlockTitle(user, requestDto.getBlockTitle()).isPresent()) {
                throw new IllegalArgumentException("이미 동일한 제목의 블록이 존재합니다.");
            }
        }

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
    public BlockPagingResponseDto<BlockListResponseDto> getBlockList(
            Optional<Block.BlockCategory> category,
            Optional<Block.TechPart> techPart,
            String keyword,
            Pageable pageable) {

        Page<Block> blockPage;

        boolean hasKeyword = keyword != null && !keyword.isEmpty();

        if (category.isPresent() && techPart.isPresent() && hasKeyword) {
            blockPage = blockRepository.findAllByCategoryNameAndTechPartAndBlockTitleContaining(
                    category.get(),
                    techPart.get(),
                    keyword,
                    pageable);
        } else {
            String finalKeyword = hasKeyword ? keyword : "";
            blockPage = blockRepository.findAllByBlockTitleContaining(finalKeyword, pageable);
        }

        Page<BlockListResponseDto> dtoPage = blockPage.map(BlockListResponseDto::new);

        return new BlockPagingResponseDto<>(dtoPage);
    }
}