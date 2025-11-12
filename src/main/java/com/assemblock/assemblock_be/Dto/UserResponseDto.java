package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Role;
import com.assemblock.assemblock_be.Entity.User;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

// "내 정보 조회"
@Getter
public class UserResponseDto {

    private Long userId;
    private String nickname;
    private List<Role> roles;
    private String introduction;
    private Integer profileImageIndex;
    private String portfolioUrl;
    private String portfolioPdfUrl;

    private Integer userLevel;
    private Integer reliabilityCnt;
    private BigDecimal reliabilityLevel;
    private Boolean isPublishing;

    // Entity -> Dto
    public UserResponseDto(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.roles = user.getRoles();
        this.introduction = user.getIntroduction();
        this.profileImageIndex = user.getProfileImageIndex();
        this.portfolioUrl = user.getPortfolioUrl();
        this.portfolioPdfUrl = user.getPortfolioPdfUrl();
        this.userLevel = user.getUserLevel();
        this.reliabilityCnt = user.getReliabilityCnt();
        this.reliabilityLevel = user.getReliabilityLevel();
        this.isPublishing = user.getIsPublishing();
    }
}