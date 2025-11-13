package com.assemblock.assemblock_be.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginRequestDto {
    private String authorizationCode; // kakao 인가코드
}