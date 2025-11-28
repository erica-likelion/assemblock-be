package com.assemblock.assemblock_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 토큰 재발급 응답 DTO
@Getter
@AllArgsConstructor
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
}