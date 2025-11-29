package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.MemberRole;
import com.assemblock.assemblock_be.Entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyProfileResponseDto {
    private String nickname;
    private String profileImageUrl;
    private String portfolioUrl;
    private String introduction;
    private MemberRole mainRole;
    private String portfolioPdfUrl;

    @Builder
    public MyProfileResponseDto(String nickname, String profileImageUrl, String portfolioUrl, String introduction, MemberRole mainRole, String portfolioPdfUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.portfolioUrl = portfolioUrl;
        this.introduction = introduction;
        this.mainRole = mainRole;
        this.portfolioPdfUrl = portfolioPdfUrl;
    }

    public static MyProfileResponseDto fromEntity(User user) {
        return MyProfileResponseDto.builder()
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .portfolioUrl(user.getPortfolioUrl())
                .introduction(user.getIntroduction())
                .mainRole(user.getMainRole())
                .portfolioPdfUrl(user.getPortfolioPdfUrl())
                .build();
    }
}