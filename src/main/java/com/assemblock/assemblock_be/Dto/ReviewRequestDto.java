package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.ReviewRating;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {
    private Long projectId;
    private Long reviewedUserId;
    private ReviewRating rating;
}