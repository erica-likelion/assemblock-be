package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.ReviewRequestDto;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    public List<Review> getWrittenReviews(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return reviewRepository.findAllByUser(user);
    }

    public List<Review> getReceivedReviews(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return reviewRepository.findAllByReviewedUser(user);
    }

    public List<Review> getReviewsByProject(Long projectId) {
        return reviewRepository.findAllByProject_Id(projectId);
    }

    @Transactional
    public Review writeReview(Long reviewerId, ReviewRequestDto dto) {

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

        return reviewRepository.save(review);
    }
}