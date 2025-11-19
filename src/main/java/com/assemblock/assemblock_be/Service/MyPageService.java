// ReviewRepository와 ProjectMemberRepository 파일 확인 필요

package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.MyProfileResponseDto;
import com.assemblock.assemblock_be.Dto.ProfileUpdateRequestDto;
import com.assemblock.assemblock_be.Dto.ReviewResponseDto;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.BlockRepository;
import com.assemblock.assemblock_be.Repository.ReviewRepository;
import com.assemblock.assemblock_be.Repository.ProjectMemberRepository;
import com.assemblock.assemblock_be.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final ReviewRepository reviewRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public MyProfileResponseDto getMyProfile(Long currentUserId) {
        User user = findUserById(currentUserId);
        return MyProfileResponseDto.fromEntity(user);
    }

    @Transactional
    public MyProfileResponseDto updateMyProfile(Long currentUserId, ProfileUpdateRequestDto requestDto) {
        User user = findUserById(currentUserId);
        MemberRole newMainRole = null;
        String mainRoleString = requestDto.getMainRole();

        if (mainRoleString != null && !mainRoleString.isBlank()) {
            try {
                newMainRole = MemberRole.valueOf(mainRoleString);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 역할(파트)입니다: " + mainRoleString);
            }
        }

        user.updateProfile(
                requestDto.getNickname(),
                requestDto.getPortfolioUrl(),
                requestDto.getIntroduction(),
                newMainRole,
                requestDto.getProfileUrl(),
                requestDto.getPortfolioPdfUrl()
        );
        return MyProfileResponseDto.fromEntity(user);
    }

    public List<BlockResponseDto> getMyBlocks(Long currentUserId, String type) {
        List<Block> blocks = findBlocksByUserIdAndType(currentUserId, type);

        return blocks.stream()
                .map(BlockMapper::blockToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getMyReviews(Long currentUserId, String type) {
        return Collections.emptyList();
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));
    }

    private List<Block> findBlocksByUserIdAndType(Long userId, String type) {
        User user = findUserById(userId);
        if ("ALL".equalsIgnoreCase(type)) {
            return blockRepository.findByUser(user);
        }
        try {
            BlockType blockType = BlockType.valueOf(type.toUpperCase());
            return blockRepository.findByUserAndBlockType(user, blockType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 블록 타입입니다: " + type);
        }
    }

    private static class BlockMapper {
        public static BlockResponseDto blockToResponseDto(Block block) {
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
}