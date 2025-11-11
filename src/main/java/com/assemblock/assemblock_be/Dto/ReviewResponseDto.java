package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Review;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewResponseDto {
    private Long reviewId;
    private String reviewerName;
    private String reviewerProfileUrl;
    private String reviewContent;
    private String projectName;
    private String memberRole;

    @Builder
    public ReviewResponseDto(Long reviewId, String reviewerName, String reviewerProfileUrl, String reviewContent, String projectName, String memberRole) {
        this.reviewId = reviewId;
        this.reviewerName = reviewerName;
        this.reviewerProfileUrl = reviewerProfileUrl;
        this.reviewContent = reviewContent;
        this.projectName = projectName;
        this.memberRole = memberRole;
    }

    public static ReviewResponseDto fromEntity(Review review, String role) {
        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .reviewerName(review.getReviewer().getNickname())
                .reviewerProfileUrl(review.getReviewer().getProfileImageUrl())
                .reviewContent(review.getReview())
                .projectName(review.getProject().getProposal().getProjectTitle())
                .memberRole(role)
                .build();
    }
}