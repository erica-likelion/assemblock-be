package com.assemblock.assemblock_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDto {
    // 2. 우리 서버가 발급하는 JWT 토큰
    private String accessToken;
    private String refreshToken;
    // 3. 2단계 정보 입력이 필요한지 여부
    private boolean isNewUser;
    private boolean isProfileComplete;
}