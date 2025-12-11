package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.ReviewRequestDto;
import com.assemblock.assemblock_be.Entity.Review;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(
            @AuthenticationPrincipal User user,
            @RequestBody ReviewRequestDto requestDto
    ) {
        Review review = reviewService.writeReview(user.getId(), requestDto);
        return ResponseEntity.ok(review);
    }

    @GetMapping
    public ResponseEntity<List<Review>> findAll() {
        List<Review> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> findById(@PathVariable Long id) {
        Review review = reviewService.findById(id);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/written")
    public ResponseEntity<List<Review>> getWrittenReviews(@AuthenticationPrincipal User user) {
        List<Review> reviews = reviewService.getWrittenReviews(user.getId());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/received")
    public ResponseEntity<List<Review>> getReceivedReviews(@AuthenticationPrincipal User user) {
        List<Review> reviews = reviewService.getReceivedReviews(user.getId());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Review>> getReviewsByProject(@PathVariable Long projectId) {
        List<Review> reviews = reviewService.getReviewsByProject(projectId);
        return ResponseEntity.ok(reviews);
    }
}