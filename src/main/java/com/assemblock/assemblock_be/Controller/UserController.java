package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.UserResponseDto;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 내 정보 조회 API
     * (헤더의 JWT 토큰을 바탕으로 인증)
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile(
            @AuthenticationPrincipal User user
    ) {
        Long currentUserId = user.getId();
        UserResponseDto responseDto = userService.getMyProfile(currentUserId);
        return ResponseEntity.ok(responseDto);
    }
}