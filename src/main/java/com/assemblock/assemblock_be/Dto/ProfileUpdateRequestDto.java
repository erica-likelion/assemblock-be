package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Role;
import com.assemblock.assemblock_be.Entity.UserProfileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ProfileUpdateRequestDto {
    private String nickname;
    private String portfolioUrl;
    private String introduction;
    private List<Role> mainRoles;
    private UserProfileType profileType;
    private String portfolioPdfUrl;
}