package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.ReviewRating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewRequestDto {
    private Long projectId;
    private Long reviewedUserId;
    private ReviewRating rating;
}