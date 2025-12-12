package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.ReviewRequestDto;
import com.assemblock.assemblock_be.Dto.ReviewResponseDto; // [필수] DTO Import
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public ReviewResponseDto findById(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        return convertToDto(review);
    }

    public List<ReviewResponseDto> findAll() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    public List<ReviewResponseDto> getWrittenReviews(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return reviewRepository.findAllByUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getReceivedReviews(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return reviewRepository.findAllByReviewedUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getReviewsByProject(Long projectId) {
        return reviewRepository.findAllByProject_Id(projectId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponseDto writeReview(Long reviewerId, ReviewRequestDto dto) {

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("작성자(Reviewer)를 찾을 수 없습니다."));

        User reviewedUser = userRepository.findById(dto.getReviewedUserId())
                .orElseThrow(() -> new IllegalArgumentException("대상자(ReviewedUser)를 찾을 수 없습니다."));

        reviewer.increaseReviewSentCnt();
        reviewedUser.increaseReviewReceivedCnt();

        Review review = Review.builder()
                .user(reviewer)
                .reviewedUser(reviewedUser)
                .project(project)
                .review(dto.getRating())
                .build();

        Review savedReview = reviewRepository.save(review);
        return convertToDto(savedReview);
    }
    private ReviewResponseDto convertToDto(Review review) {
        User targetUser = review.getReviewedUser();
        Project project = review.getProject();

        String roleName = "워크스페이스";
        if (project != null) {
            Optional<ProjectMember> memberOpt = projectMemberRepository.findByProjectAndUser(project, targetUser);
            if (memberOpt.isPresent() && memberOpt.get().getIsProposer()) {
                roleName = "크리에이터";
            }
        }

        return ReviewResponseDto.fromEntity(review, targetUser, roleName);
    }
}