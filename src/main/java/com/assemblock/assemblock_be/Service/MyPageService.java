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
import org.springframework.web.multipart.MultipartFile;

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

        List<Role> convertedRoles = null;
        if (requestDto.getMainRoles() != null) {
            convertedRoles = requestDto.getMainRoles().stream()
                    .map(memberRole -> Role.valueOf(memberRole.name()))
                    .collect(Collectors.toList());
        }

        user.updateProfile(
                requestDto.getNickname(),
                requestDto.getPortfolioUrl(),
                requestDto.getIntroduction(),
                convertedRoles,
                requestDto.getProfileType(),
                requestDto.getPortfolioPdfUrl()
        );
        return MyProfileResponseDto.fromEntity(user);
    }

    @Transactional
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        // 임시 URL 로직 (S3 설정 시 실제 로직으로 대체)
        String fileName = file.getOriginalFilename();
        return "https://s3.amazonaws.com/assemblock-bucket/" + fileName;
    }

    public List<BlockResponseDto> getMyBlocks(Long currentUserId, String type) {
        User user = findUserById(currentUserId);
        List<Block> blocks;

        if ("ALL".equalsIgnoreCase(type) || type == null) {
            blocks = blockRepository.findAllByUser(user);
        } else {
            try {
                Block.BlockType blockType = Block.BlockType.valueOf(type.toUpperCase());
                blocks = blockRepository.findAllByUserAndBlockType(user, blockType);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 블록 타입입니다: " + type);
            }
        }

        return blocks.stream()
                .map(BlockResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getMyReviews(Long currentUserId, String type) {
        User currentUser = findUserById(currentUserId);
        List<Review> reviewsToProcess;

        if ("SCOUTING".equalsIgnoreCase(type)) {
            reviewsToProcess = reviewRepository.findAllByUser(currentUser);
        } else if ("PARTICIPATION".equalsIgnoreCase(type)) {
            reviewsToProcess = reviewRepository.findAllByReviewedUser(currentUser);
        } else {
            throw new IllegalArgumentException("유효하지 않은 리뷰 타입입니다: " + type);
        }

        return reviewsToProcess.stream()
                .map(review -> {
                    Project project = review.getProject();
                    User targetUser = "SCOUTING".equalsIgnoreCase(type) ? review.getReviewedUser() : review.getUser();

                    Optional<ProjectMember> roleOpt = projectMemberRepository.findByProjectAndUser(project, targetUser);
                    if (roleOpt.isEmpty()) return null;

                    ProjectMember member = roleOpt.get();
                    boolean isProposer = member.getIsProposer();
                    String roleName = isProposer ? "크리에이터" : "워크스페이스";

                    return ReviewResponseDto.fromEntity(review, targetUser, roleName);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));
    }
}