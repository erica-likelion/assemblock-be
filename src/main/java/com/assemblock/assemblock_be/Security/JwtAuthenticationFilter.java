package com.assemblock.assemblock_be.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 이 필터는 HTTP 요청마다 한 번씩 실행됩니다.
// 요청 헤더의 'Authorization' 헤더에서 JWT 토큰을 추출하여 유효성을 검사합니다.
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService; // Spring Security가 사용자를 찾는 서비스

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            // 토큰이 유효한지 확인
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                // 토큰에서 사용자 ID 추출
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);

                // UserDetailsService를 통해 UserDetails 객체(우리 앱에서는 User 객체) 로드
                // UserDetailsService는 DB에서 ID(여기서는Long)로 User를 찾아야 합니다.
                // (주의: 기본 UserDetailsService는 username(String)으로 찾으므로 Custom UserDetailsService가 필요)
                UserDetails userDetails = userDetailsService.loadUserByUsername(Long.toString(userId));

                // Spring Security Context에 인증 정보 저장
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // (로그) logger.error("Security Context에 인증 정보를 세팅할 수 없습니다.", ex);
        }

        filterChain.doFilter(request, response);
    }

    // "Authorization: Bearer [토큰]" 헤더에서 "Bearer " 부분을 제거하고 토큰 값만 반환
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}