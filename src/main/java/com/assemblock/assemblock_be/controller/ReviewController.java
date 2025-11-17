package com.assemblblock.assemblblock_be.controller;

import com.assemblblock.assemblblock_be.entity.Review;
import com.assemblblock.assemblblock_be.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping
    public Review create(@RequestBody Review review) {
        return reviewService.create(review);
    }

    // 리뷰 조회
    @GetMapping("/{id}")
    public Review findById(@PathVariable Long id) {
        return reviewService.findById(id);
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reviewService.delete(id);
    }
}
