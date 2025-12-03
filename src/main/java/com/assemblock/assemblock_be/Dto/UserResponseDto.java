package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Role;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Entity.UserProfileType;
import lombok.Getter;

import java.util.List;

@Getter
public class UserResponseDto {

    private Long userId;
    private String nickname;
    private List<Role> roles;
    private String introduction;
    private UserProfileType profileType;
    private String portfolioUrl;
    private String portfolioPdfUrl;

    private Integer reviewSentCnt;
    private Integer reviewReceivedCnt;
    private Boolean isPublishing;

    public UserResponseDto(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.roles = user.getRoles();
        this.introduction = user.getIntroduction();
        this.profileType = user.getProfileType();
        this.portfolioUrl = user.getPortfolioUrl();
        this.portfolioPdfUrl = user.getPortfolioPdfUrl();
        this.reviewSentCnt = user.getReviewSentCnt();
        this.reviewReceivedCnt = user.getReviewReceivedCnt();
        this.isPublishing = user.getIsPublishing();
    }
}