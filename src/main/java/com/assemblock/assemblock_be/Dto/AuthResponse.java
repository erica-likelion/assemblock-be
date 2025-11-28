package com.assemblock.assemblock_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private boolean isNewUser;
    private boolean isProfileComplete;
}