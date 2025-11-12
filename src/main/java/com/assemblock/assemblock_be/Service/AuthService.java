package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.*; // Dto 임포트
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Repository.UserRepository;
import com.assemblock.assemblock_be.Security.JwtTokenProvider;
import io.jsonwebtoken.JwtException; // 예외 임포트
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    // ... (기존 final 필드 ... )
    private final WebClient webClient;
    private final JwtTokenProvider jwtTokenProvider;

    // ... (기존 @Value 필드 ... )
    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;


    /**
     * 1단계: 카카오 로그인 (신규 유저 시 자동 회원가입)
     */
    @Transactional
// ... (기존 kakaoLogin 메서드 ... )
    public AuthResponseDto kakaoLogin(String authorizationCode) {

        // ... (기존 1~5 로직 ... )
        String kakaoAccessToken = getKakaoAccessToken(authorizationCode);
        Long kakaoId = getKakaoUserId(kakaoAccessToken);
        Optional<User> existingUser = userRepository.findByKakaoId(kakaoId);

        boolean isNewUser = existingUser.isEmpty();
        User user;

        if (isNewUser) {
            user = User.builder()
                    .kakaoId(kakaoId)
                    .build();
            userRepository.save(user);
        } else {
            user = existingUser.get();
        }

        // 6. 우리 서비스의 JWT 토큰 발급 (Access/Refresh)
        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        // 7. DTO에 담아 응답
        return new AuthResponseDto(
// ... (기존 return ... )
                accessToken,
                refreshToken,
                isNewUser,
                user.isProfileComplete()
        );
    }

    /**
     * 2단계: 추가 정보 입력 (프로필 완성)
     */
    @Transactional
// ... (기존 completeProfile 메서드 ... )
    public void completeProfile(Long userId, SignupRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + userId));
        user.completeProfile(requestDto);
    }

    /**
     * (신규) 액세스 토큰 재발급
     */
    @Transactional
    public TokenRefreshResponseDto refreshAccessToken(String refreshToken) {
        // 1. 리프레시 토큰 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new JwtException("Invalid Refresh Token");
        }

        // 2. 토큰에서 UserId 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        // 3. DB에서 유저 조회 (필수! 탈퇴한 유저일 수 있음)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + userId));

        // 4. 새 액세스 토큰 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId());

        // (선택적) 리프레시 토큰 로테이션: 새 리프레시 토큰을 발급할 수도 있음
        // String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        return new TokenRefreshResponseDto(newAccessToken, refreshToken); // (현재는 기존 RT 반환)
    }


    // --- ▽ 카카오 API 통신 (WebClient) ▽ ---
// ... (기존 getKakaoAccessToken, getKakaoUserId 메서드 ... )
    private String getKakaoAccessToken(String code) {
// ... (기존 ... )
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        Map<String, String> response = webClient.post()
                .uri(tokenUri)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue("grant_type=authorization_code" +
                        "&client_id=" + kakaoRestApiKey +
                        "&redirect_uri=" + kakaoRedirectUri +
                        "&code=" + code)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return (String) response.get("access_token");
    }
    private Long getKakaoUserId(String accessToken) {
// ... (기존 ... )
        String userUri = "https://kapi.kakao.com/v2/user/me";

        Map<String, Object> response = webClient.get()
                .uri(userUri)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return ((Number) response.get("id")).longValue();
    }
}