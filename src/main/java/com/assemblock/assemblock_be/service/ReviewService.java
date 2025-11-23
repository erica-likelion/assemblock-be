package com.assemblock.assemblock_be.service;

import com.assemblock.assemblock_be.entity.Review;
import com.assemblock.assemblock_be.entity.ReviewStatus;
import com.assemblock.assemblock_be.entity.Project;
import com.assemblock.assemblock_be.entity.User;
import com.assemblock.assemblock_be.repository.ReviewRepository;
import com.assemblock.assemblock_be.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProjectRepository projectRepository;

    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    public Review writeReview(Long reviewerId, Long reviewedUserId, Long projectId, String text) {

        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트 없음"));

        Review review = new Review();
        review.setReviewer(new User(reviewerId));
        review.setReviewedUser(new User(reviewedUserId));
        review.setProject(p);
        review.setReviewStatus(ReviewStatus.valueOf(text));


        return reviewRepository.save(review);
    }
}
