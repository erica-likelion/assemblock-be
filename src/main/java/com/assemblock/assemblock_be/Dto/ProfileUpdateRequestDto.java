package com.assemblock.assemblock_be.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProfileUpdateRequestDto {
    private String nickname;
    private String portfolioUrl;
    private String introduction;
    private String mainRole;
    private String profileUrl;
    private String portfolioPdfUrl;
}