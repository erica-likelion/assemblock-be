package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Role;
import com.assemblock.assemblock_be.Entity.UserProfileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 5, message = "닉네임은 최대 5자까지 가능합니다.")
    private String nickname;

    @NotNull(message = "역할은 최소 1개 이상 선택해야 합니다.")
    private List<Role> roles;

    @NotNull(message = "프로필 이미지를 선택해야 합니다.")
    private UserProfileType userProfileType;

    @Size(max = 36, message = "한줄 소개는 최대 36자까지 가능합니다.")
    private String introduction;

    private String portfolioUrl;
    private String portfolioPdfUrl;
}