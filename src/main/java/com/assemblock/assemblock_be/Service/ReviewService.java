package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.ReviewRequestDto;
import com.assemblock.assemblock_be.Entity.Project;
import com.assemblock.assemblock_be.Entity.Review;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Repository.ProjectRepository;
import com.assemblock.assemblock_be.Repository.ReviewRepository;
import com.assemblock.assemblock_be.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public void createReview(Long reviewerId, ReviewRequestDto requestDto) {
        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        User reviewedUser = userRepository.findById(requestDto.getReviewedUserId())
                .orElseThrow(() -> new IllegalArgumentException("대상 사용자를 찾을 수 없습니다."));

        Project project = projectRepository.findById(requestDto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        if (reviewRepository.existsByProjectAndUserAndReviewedUser(project, reviewer, reviewedUser)) {
            throw new IllegalStateException("이미 해당 사용자에게 리뷰를 작성했습니다.");
        }

        Review review = Review.builder()
                .user(reviewer)
                .reviewedUser(reviewedUser)
                .project(project)
                .review(requestDto.getRating())
                .build();

        reviewRepository.save(review);

        reviewer.increaseReviewSentCnt();
        reviewedUser.increaseReviewReceivedCnt();
    }
}