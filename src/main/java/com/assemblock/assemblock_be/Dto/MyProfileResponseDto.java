package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Role;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Entity.UserProfileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyProfileResponseDto {
    private String nickname;
    private UserProfileType profileType;
    private String portfolioUrl;
    private String introduction;
    private List<Role> mainRoles;
    private String portfolioPdfUrl;
    private Integer reviewSentCnt;
    private Integer reviewReceivedCnt;

    public static MyProfileResponseDto fromEntity(User user) {
        return MyProfileResponseDto.builder()
                .nickname(user.getNickname())
                .profileType(user.getProfileType())
                .portfolioUrl(user.getPortfolioUrl())
                .introduction(user.getIntroduction())
                .mainRoles(user.getRoles())
                .portfolioPdfUrl(user.getPortfolioPdfUrl())
                .reviewSentCnt(user.getReviewSentCnt())
                .reviewReceivedCnt(user.getReviewReceivedCnt())
                .build();
    }
}