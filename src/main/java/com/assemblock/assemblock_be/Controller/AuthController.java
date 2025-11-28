package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.*;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kakao")
    public ResponseEntity<AuthResponseDto> kakaoLogin(
            @RequestBody KakaoLoginRequestDto requestDto
    ) {
        AuthResponseDto responseDto = authService.kakaoLogin(requestDto.getAuthorizationCode());
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/signup")
    public ResponseEntity<String> signup(
            @Valid @RequestBody SignupRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        Long currentUserId = user.getId();
        authService.completeProfile(currentUserId, requestDto);
        return ResponseEntity.ok("Profile setup complete.");
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponseDto> refreshAccessToken(
            @Valid @RequestBody TokenRefreshRequestDto requestDto
    ) {
        TokenRefreshResponseDto responseDto = authService.refreshAccessToken(requestDto.getRefreshToken());
        return ResponseEntity.ok(responseDto);
    }
}