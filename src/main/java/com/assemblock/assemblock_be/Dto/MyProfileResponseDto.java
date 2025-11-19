package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyProfileResponseDto {
    private String nickname;
    private String profileUrl;
    private String portfolioUrl;
    private String introduction;
    private String mainRole;
    private String portfolioPdfUrl;
    private Integer userLevel;
    private Integer reliabilityCnt;
    private Double reliabilityLevel;
    private Boolean isPublishing;

    @Builder
    public MyProfileResponseDto(
            String nickname,
            String profileUrl,
            String portfolioUrl,
            String introduction,
            String mainRole,
            String portfolioPdfUrl,
            Integer userLevel,
            Integer reliabilityCnt,
            Double reliabilityLevel,
            Boolean isPublishing) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.portfolioUrl = portfolioUrl;
        this.introduction = introduction;
        this.mainRole = mainRole;
        this.portfolioPdfUrl = portfolioPdfUrl;
        this.userLevel = userLevel;
        this.reliabilityCnt = reliabilityCnt;
        this.reliabilityLevel = reliabilityLevel;
        this.isPublishing = isPublishing;
    }
    public static MyProfileResponseDto fromEntity(User user) {
        Double reliabilityLevel = user.getReliabilityLevel() != null
                ? user.getReliabilityLevel().doubleValue()
                : null;

        return MyProfileResponseDto.builder()
                .nickname(user.getNickname())
                .profileUrl(user.getProfileImageUrl())
                .portfolioUrl(user.getPortfolioUrl())
                .introduction(user.getIntroduction())
                .mainRole(user.getMainRole().name())
                .portfolioPdfUrl(user.getPortfolioPdfUrl())
                .userLevel(user.getUserLevel())
                .reliabilityCnt(user.getReliabilityCnt())
                .reliabilityLevel(reliabilityLevel)
                .isPublishing(user.getIsPublishing())
                .build();
    }
}