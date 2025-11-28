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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kakao")
    public ResponseEntity<AuthResponse> kakaoLogin(
            @RequestBody KakaoLoginDto requestDto
    ) {
        AuthResponse responseDto = authService.kakaoLogin(requestDto.getAuthorizationCode());
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/signup")
    public ResponseEntity<String> signup(
            @Valid @RequestBody SignupDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        Long currentUserId = user.getId();
        authService.completeProfile(currentUserId, requestDto);
        return ResponseEntity.ok("Profile setup complete.");
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshAccessToken(
            @Valid @RequestBody TokenRefreshDto requestDto
    ) {
        TokenRefreshResponse responseDto = authService.refreshAccessToken(requestDto.getRefreshToken());
        return ResponseEntity.ok(responseDto);
    }
}