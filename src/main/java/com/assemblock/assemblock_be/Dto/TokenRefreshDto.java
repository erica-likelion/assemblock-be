package com.assemblock.assemblock_be.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshDto {

    @NotBlank(message = "Refresh Token은 필수입니다.")
    private String refreshToken;
}