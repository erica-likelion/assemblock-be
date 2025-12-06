package com.assemblock.assemblock_be.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoLoginDto {
    private String authorizationCode;
}