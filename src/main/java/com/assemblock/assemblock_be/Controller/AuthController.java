package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.*; // Dto 임포트
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*; // PostMapping 임포트

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 1단계: 카카오 로그인
     */
    @PostMapping("/kakao")
    public ResponseEntity<AuthResponseDto> kakaoLogin(
            @RequestBody KakaoLoginRequestDto requestDto
    ) {
        AuthResponseDto responseDto = authService.kakaoLogin(requestDto.getAuthorizationCode());
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 2단계: 추가 정보 입력 (회원가입/프로필 수정)
     */
    @PutMapping("/signup")
// ... (기존 signup 메서드 ... )
    public ResponseEntity<String> signup(
            @Valid @RequestBody SignupRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        Long currentUserId = user.getId();
        authService.completeProfile(currentUserId, requestDto);
        return ResponseEntity.ok("Profile setup complete.");
    }

    /**
     * 3. (신규) 액세스 토큰 재발급 API
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponseDto> refreshAccessToken(
            @Valid @RequestBody TokenRefreshRequestDto requestDto
    ) {
        TokenRefreshResponseDto responseDto = authService.refreshAccessToken(requestDto.getRefreshToken());
        return ResponseEntity.ok(responseDto);
    }
}