package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.ReviewRequestDto;
import com.assemblock.assemblock_be.Dto.ReviewResponseDto;
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
    public ResponseEntity<ReviewResponseDto> createReview(
            @AuthenticationPrincipal User user,
            @RequestBody ReviewRequestDto requestDto
    ) {
        ReviewResponseDto review = reviewService.writeReview(user.getId(), requestDto);
        return ResponseEntity.ok(review);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> findAll() {
        List<ReviewResponseDto> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> findById(@PathVariable Long id) {
        ReviewResponseDto review = reviewService.findById(id);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/written")
    public ResponseEntity<List<ReviewResponseDto>> getWrittenReviews(@AuthenticationPrincipal User user) {
        List<ReviewResponseDto> reviews = reviewService.getWrittenReviews(user.getId());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/received")
    public ResponseEntity<List<ReviewResponseDto>> getReceivedReviews(@AuthenticationPrincipal User user) {
        List<ReviewResponseDto> reviews = reviewService.getReceivedReviews(user.getId());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProject(@PathVariable Long projectId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByProject(projectId);
        return ResponseEntity.ok(reviews);
    }
}