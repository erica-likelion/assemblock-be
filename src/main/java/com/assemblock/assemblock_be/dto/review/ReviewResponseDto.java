package com.assemblock.assemblock_be.dto.review;

import com.assemblock.assemblock_be.entity.ReviewStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {

    private Long reviewId;
    private Long reviewerId;
    private Long reviewedUserId;
    private Long projectId;
    private ReviewStatus reviewStatus;
}
