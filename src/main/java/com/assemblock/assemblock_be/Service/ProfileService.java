package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.MyProfileResponseDto;
import com.assemblock.assemblock_be.Dto.ReviewResponseDto;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.BlockRepository;
import com.assemblock.assemblock_be.Repository.ProjectMemberRepository;
import com.assemblock.assemblock_be.Repository.ReviewRepository;
import com.assemblock.assemblock_be.Repository.UserRepository;
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
                .map(block -> BlockResponseDto.builder()
                        .blockId(block.getId())
                        .title(block.getTitle())
                        .description(block.getOnelineSummary())
                        .username(block.getUser().getNickname())
                        .coverImageUrl(block.getResultUrl())
                        .build())
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
                .map(review -> {
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
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private User findPublicUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        if (!user.getIsPublishing()) {
            throw new IllegalArgumentException("비공개 프로필입니다.");
        }
        return user;
    }
}