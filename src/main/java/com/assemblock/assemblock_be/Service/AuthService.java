package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.*;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Repository.UserRepository;
import com.assemblock.assemblock_be.Security.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;
import java.util.Optional;

@Slf4j
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
                    .nickname("User_" + kakaoId) // 닉네임 guest1234 -> user1234로 변경
                    .build();
            userRepository.save(user);
        } else {
            user = existingUser.get();
        }
        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        user.updateRefreshToken(refreshToken);

        return new AuthResponseDto(
                accessToken,
                refreshToken,
                user.isProfileComplete()
        );
    }

    @Transactional
    public void completeProfile(Long userId, SignupDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + userId));
        user.completeProfile(requestDto);
    }

    @Transactional
    public TokenRefreshResponseDto refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new JwtException("Invalid Refresh Token");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + userId));

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new JwtException("Refresh Token does not match");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getId());

        return new TokenRefreshResponseDto(newAccessToken, refreshToken);
    }


    private String getKakaoAccessToken(String code) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoRestApiKey);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        try {
            Map response = webClient.post()
                    .uri(tokenUri)
                    .contentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(params))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return (String) response.get("access_token");

        } catch (WebClientResponseException e) {
            log.error("Kakao Token Error: {}", e.getResponseBodyAsString());
            throw new RuntimeException("카카오 토큰 발급 실패 (KOE001): " + e.getResponseBodyAsString());
        }
    }

    private Long getKakaoUserId(String accessToken) {
        String userUri = "https://kapi.kakao.com/v2/user/me";

        Map response = webClient.get()
                .uri(userUri)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("id")) {
            throw new RuntimeException("Failed to retrieve Kakao user info");
        }

        return ((Number) response.get("id")).longValue();
    }
}