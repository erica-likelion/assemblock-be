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
    private final WebClient webClient;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;


    /**
     * 1단계: 카카오 로그인 (신규 유저 시 자동 회원가입)
     */
    @Transactional
    public AuthResponseDto kakaoLogin(String authorizationCode) {

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

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        return new AuthResponseDto(
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
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new JwtException("Invalid Refresh Token");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + userId));

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId());

        return new TokenRefreshResponseDto(newAccessToken, refreshToken);
    }

    private String getKakaoAccessToken(String code) {
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