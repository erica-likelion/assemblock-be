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

        if ("ALL".equalsIgnoreCase(type) || type == null) {
            blocks = blockRepository.findAllByUser(user);
        } else {
            try {
                BlockType blockType = BlockType.valueOf(type.toUpperCase());
                blocks = blockRepository.findAllByUserAndBlockType(user, blockType);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 블록 타입입니다: " + type);
            }
        }

        return blocks.stream()
                .map(BlockResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getPublicReviews(Long userId, String type) {
        User user = findPublicUserById(userId);
        List<Review> reviewsToProcess;

        if ("SCOUTING".equalsIgnoreCase(type)) {
            reviewsToProcess = reviewRepository.findAllByUser(user);
        } else if ("PARTICIPATION".equalsIgnoreCase(type)) {
            reviewsToProcess = reviewRepository.findAllByReviewedUser(user);
        } else {
            throw new IllegalArgumentException("유효하지 않은 리뷰 타입입니다: " + type);
        }

        return reviewsToProcess.stream()
                .map(review -> {
                    Project project = review.getProject();

                    User targetUser = "SCOUTING".equalsIgnoreCase(type) ? review.getReviewedUser() : review.getUser();

                    Optional<ProjectMember> roleOpt = projectMemberRepository.findByProjectAndUser(
                            project, targetUser
                    );

                    if (roleOpt.isEmpty()) {
                        return null;
                    }

                    ProjectMember member = roleOpt.get();
                    boolean isProposer = member.getIsProposer();
                    String roleName = isProposer ? "크리에이터" : "워크스페이스";

                    return ReviewResponseDto.fromEntity(review, targetUser, roleName);
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