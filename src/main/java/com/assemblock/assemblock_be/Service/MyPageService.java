package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.MyProfileResponseDto;
import com.assemblock.assemblock_be.Dto.ProfileUpdateRequestDto;
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
        if (requestDto.getMainRole() != null && !requestDto.getMainRole().isBlank()) {
            try {
                newMainRole = MemberRole.valueOf(requestDto.getMainRole());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 역할(파트)입니다: " + requestDto.getMainRole());
            }
        }

        user.updateProfile(
                requestDto.getNickname(),
                requestDto.getPortfolioUrl(),
                requestDto.getIntroduction(),
                newMainRole,
                requestDto.getProfileImageUrl(),
                requestDto.getPortfolioPdfUrl()
        );
        return MyProfileResponseDto.fromEntity(user);
    }

    public List<BlockResponseDto> getMyBlocks(Long currentUserId, String type) {
        User user = findUserById(currentUserId);
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

    public List<ReviewResponseDto> getMyReviews(Long currentUserId, String type) {
        User currentUser = findUserById(currentUserId);
        List<Review> reviewsToProcess;
        if ("SCOUTING".equalsIgnoreCase(type)) {
            reviewsToProcess = reviewRepository.findByReviewer(currentUser);
        } else if ("PARTICIPATION".equalsIgnoreCase(type)) {
            reviewsToProcess = reviewRepository.findByUser(currentUser);
        } else {
            throw new IllegalArgumentException("유효하지 않은 리뷰 타입입니다: " + type);
        }

        return reviewsToProcess.stream()
                .map(review -> {
                    Project project = review.getProject();
                    Optional<ProjectMember> myRoleInProjectOpt = projectMemberRepository.findByProjectAndUser(
                            project, currentUser
                    );
                    if (myRoleInProjectOpt.isEmpty()) {
                        return null;
                    }

                    ProjectMember myRole = myRoleInProjectOpt.get();
                    boolean amIProposer = myRole.getIsProposer();
                    String roleName = amIProposer ? "크리에이터" : "워크스페이스";

                    return ReviewResponseDto.fromEntity(review, roleName);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));
    }
}