package com.assemblock.assemblock_be.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginRequestDto {
    // 1. 클라이언트가 카카오 서버에서 받아오는 인가 코드
    private String authorizationCode;
}