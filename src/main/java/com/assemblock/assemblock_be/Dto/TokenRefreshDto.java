package com.assemblock.assemblock_be.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

// 토큰 재발급 요청 DTO
@Getter
public class TokenRefreshDto {

    @NotBlank(message = "Refresh Token은 필수입니다.")
    private String refreshToken;
}