package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.MemberRole;
import com.assemblock.assemblock_be.Entity.ProfileType;
import com.assemblock.assemblock_be.Entity.Review;
import com.assemblock.assemblock_be.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String targetUserNickname;
    private ProfileType targetUserProfileType;
    private MemberRole targetUserMainRole;
    private String reviewContent;
    private String projectName;
    private String memberRole;

    public static ReviewResponseDto fromEntity(Review review, User targetUser, String projectRole) {
        MemberRole mainRole = targetUser.getMainRoles().stream().findFirst().orElse(MemberRole.BackEnd);

        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .targetUserNickname(targetUser.getNickname())
                .targetUserProfileType(targetUser.getProfileType())
                .targetUserMainRole(mainRole)
                .reviewContent(review.getReview().name())
                .projectName(review.getProject().getProposal().getProjectTitle())
                .memberRole(projectRole)
                .build();
    }
}