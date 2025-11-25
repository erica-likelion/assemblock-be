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
public class ReviewCreateRequestDto {

    private Long reviewerId;      // 리뷰를 남기는 사람
    private Long reviewedUserId;  // 리뷰를 받는 사람
    private Long projectId;       // 함께 했던 프로젝트 id
    private ReviewStatus reviewStatus; // good / notbad / disappoint
}
