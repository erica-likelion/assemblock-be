package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Review;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    private Long reviewId;
    private String nickname;
    private String profileUrl;
    private String review;
    private String projectTitle;
    private String memberRole;
    private LocalDateTime createdAt;

    @Builder
    public ReviewResponseDto(Long reviewId, String nickname, String profileUrl, String review, String projectTitle, String memberRole, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.review = review;
        this.projectTitle = projectTitle;
        this.memberRole = memberRole;
        this.createdAt = createdAt;
    }

    public static ReviewResponseDto fromEntity(Review review, String memberRole) {
        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .nickname(review.getReviewedUser().getNickname())
                .profileUrl(review.getReviewedUser().getProfileImageUrl())
                .review(review.getReview())
                .projectTitle(review.getProject().getProposal().getProjectTitle())
                .memberRole(memberRole)
                .createdAt(review.getCreatedAt())
                .build();
    }
}