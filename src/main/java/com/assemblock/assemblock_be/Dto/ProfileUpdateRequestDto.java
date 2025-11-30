package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.MemberRole;
import com.assemblock.assemblock_be.Entity.UserProfileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class ProfileUpdateRequestDto {
    private String nickname;
    private String portfolioUrl;
    private String introduction;
    private Set<MemberRole> mainRoles;
    private UserProfileType profileType;
    private String portfolioPdfUrl;
}