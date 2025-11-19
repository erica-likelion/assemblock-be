// ProjectMemberRepository와 ReviewRepository 파일 확인 필요

package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.MyProfileResponseDto;
import com.assemblock.assemblock_be.Dto.ReviewResponseDto;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Entity.TechPart.TechName;
import com.assemblock.assemblock_be.Repository.BlockRepository;
import com.assemblock.assemblock_be.Repository.UserRepository;
import com.assemblock.assemblock_be.Repository.ProjectMemberRepository;
import com.assemblock.assemblock_be.Repository.ReviewRepository;
import com.assemblock.assemblock_be.Repository.UserTechPartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final ReviewRepository reviewRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TechPartService techPartService;
    private final UserTechPartRepository userTechPartRepository;

    public MyProfileResponseDto getPublicProfile(Long userId) {
        User user = findPublicUserById(userId);
        return MyProfileResponseDto.fromEntity(user);
    }

    public List<BlockResponseDto> getPublicBlocks(Long userId, String type) {
        User user = findPublicUserById(userId);
        List<Block> blocks;
        if ("ALL".equalsIgnoreCase(type)) {
            blocks = blockRepository.findByUser(user);
        } else {
            try {
                BlockType blockType = BlockType.valueOf(type.toUpperCase());
                blocks = blockRepository.findByUserAndBlockType(user, blockType);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 블록 타입입니다: " + type);
            }
        }

        return blocks.stream()
                .map(this::blockToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getPublicReviews(Long userId, String type) {
        User user = findPublicUserById(userId);
        List<Review> reviewsToProcess;
        if ("SCOUTING".equalsIgnoreCase(type)) {
            reviewsToProcess = reviewRepository.findByReviewer(user);
        } else if ("PARTICIPATION".equalsIgnoreCase(type)) {
            reviewsToProcess = reviewRepository.findByUser(user);
        } else {
            throw new IllegalArgumentException("유효하지 않은 리뷰 타입입니다: " + type);
        }

        return reviewsToProcess.stream()
                .map(review -> mapReviewToResponse(review, user))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserTechParts(Long userId, List<String> techNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        userTechPartRepository.deleteAllByUser(user);
        for (String name : techNames) {
            TechName techEnum = TechName.valueOf(name.toUpperCase());
            TechPart techPart = techPartService.findTechPartByEnum(techEnum);

            UserTechPart userTechPart = UserTechPart.builder()
                    .user(user)
                    .techPart(techPart)
                    .build();
            userTechPartRepository.save(userTechPart);
        }
    }

    private User findPublicUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        if (!user.getIsPublishing()) {
            throw new IllegalArgumentException("비공개 프로필입니다.");
        }
        return user;
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

    private ReviewResponseDto mapReviewToResponse(Review review, User user) {
        Project project = review.getProject();
        Optional<ProjectMember> userRoleInProjectOpt = projectMemberRepository.findByProjectAndUser(
                project, user
        );
        if (userRoleInProjectOpt.isEmpty()) {
            return null;
        }

        ProjectMember userRole = userRoleInProjectOpt.get();
        boolean isProposer = userRole.getIsProposer();
        String roleName = isProposer ? "크리에이터" : "워크스페이스";
        return ReviewResponseDto.fromEntity(review, roleName);
    }
}