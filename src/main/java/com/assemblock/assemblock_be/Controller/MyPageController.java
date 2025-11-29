package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.MyProfileResponseDto;
import com.assemblock.assemblock_be.Dto.ProfileUpdateRequestDto;
import com.assemblock.assemblock_be.Dto.ReviewResponseDto;
import com.assemblock.assemblock_be.Service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal; // 카카오로그인 구현 후 수정
import com.assemblock.assemblock_be.security.UserDetailsImpl; // 카카오로그인 구현 후 수정

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    /**
     * 내 프로필 정보 조회
     * [GET] /api/mypage/profile
     */
    @GetMapping("/profile")
    public ResponseEntity<MyProfileResponseDto> getMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails // 카카오로그인 구현 후 수정
    ) {
        Long currentUserId = userDetails.getUserId();
        MyProfileResponseDto responseDto = myPageService.getMyProfile(currentUserId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 내 프로필 정보 수정
     * [PUT] /api/mypage/profile
     */
    @PutMapping("/profile")
    public ResponseEntity<MyProfileResponseDto> updateMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails, // 카카오로그인 구현 후 수정
            @RequestBody ProfileUpdateRequestDto requestDto
    ) {
        Long currentUserId = userDetails.getUserId();
        MyProfileResponseDto responseDto = myPageService.updateMyProfile(currentUserId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 나의 어셈블록 목록 조회
     * [GET] /api/mypage/blocks?type={blockType}
     */
    @GetMapping("/blocks")
    public ResponseEntity<List<BlockResponseDto>> getMyBlocks(
            @AuthenticationPrincipal UserDetailsImpl userDetails, // 카카오로그인 구현 후 수정
            @RequestParam(defaultValue = "ALL") String type
    ) {
        Long currentUserId = userDetails.getUserId();
        List<BlockResponseDto> blocks = myPageService.getMyBlocks(currentUserId, type);
        return ResponseEntity.ok(blocks);
    }

    /**
     * API: 나의 후기블록 목록 조회
     * [GET] /api/mypage/reviews?type={reviewType}
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam String type
    ) {
        Long currentUserId = userDetails.getUserId();
        List<ReviewResponseDto> reviews = myPageService.getMyReviews(currentUserId, type);
        return ResponseEntity.ok(reviews);
    }
}