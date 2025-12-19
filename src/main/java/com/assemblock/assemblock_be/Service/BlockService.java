package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockDto;
import com.assemblock.assemblock_be.Dto.BlockResponseDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public List<BlockResponseDto> findBlocks(
            String blockTypeStr,
            Block.BlockCategory category,
            Block.TechPart techPart,
            String keyword
    ) {
        Block.BlockType blockType = null;
        if (StringUtils.hasText(blockTypeStr)) {
            try {
                blockType = Block.BlockType.valueOf(blockTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return List.of();
            }
        }

        String finalKeyword = StringUtils.hasText(keyword) ? keyword : null;

        List<Block> blocks = blockRepository.findBlocksDynamic(
                blockType,
                category,
                techPart,
                finalKeyword
        );

        return blocks.stream()
                .map(BlockResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BlockResponseDto> findAll(String blockTypeStr) {
        List<Block> blocks;

        if (blockTypeStr != null && !blockTypeStr.isEmpty()) {
            try {
                Block.BlockType type = Block.BlockType.valueOf(blockTypeStr.toUpperCase());
                blocks = blockRepository.findAllByBlockType(type);
            } catch (IllegalArgumentException e) {
                return List.of();
            }
        } else {
            blocks = blockRepository.findAll();
        }

        return blocks.stream()
                .map(BlockResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createBlock(Long userId, BlockDto requestDto, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        validateBlockTypeRequirements(requestDto);

        String fileUrl = null;
        String originalFileName = null;

        if (file != null && !file.isEmpty()) {
            fileUrl = s3Service.uploadFile(file);
            originalFileName = file.getOriginalFilename();
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
                .resultFile(fileUrl)
                .resultFileName(originalFileName)
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
    public void updateBlock(Long userId, Long blockId, BlockDto requestDto, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));

        if (!block.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User does not have permission to update this block");
        }

        validateBlockTypeRequirements(requestDto);

        String fileUrl = block.getResultFile();
        String originalFileName = block.getResultFileName();

        if (file != null && !file.isEmpty()) {
            if (StringUtils.hasText(fileUrl)) {
                s3Service.deleteFile(fileUrl);
            }
            fileUrl = s3Service.uploadFile(file);
            originalFileName = file.getOriginalFilename();
        }

        block.update(requestDto, fileUrl, originalFileName);
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

        if (StringUtils.hasText(block.getResultFile())) {
            s3Service.deleteFile(block.getResultFile());
        }

        blockRepository.delete(block);
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