package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class SignupRequestDto {

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 5, message = "닉네임은 최대 5자까지 가능합니다.")
    private String nickname;

    @NotNull(message = "역할은 최소 1개 이상 선택해야 합니다.")
    private List<Role> roles;

    @NotNull(message = "프로필 이미지를 선택해야 합니다.")
    private Integer profileImageIndex; // 0~4

    @Size(max = 36, message = "한줄 소개는 최대 36자까지 가능합니다.")
    private String introduction;

    // 선택 사항 (null 가능)
    private String portfolioUrl;
    private String portfolioPdfUrl;
}