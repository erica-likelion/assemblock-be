// 카카오로그인 구현 후 수정

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
import com.assemblock.assemblock_be.Security.UserDetailsImpl; // 카카오로그인 구현 후 수정

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/profile")
    public ResponseEntity<MyProfileResponseDto> getMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long currentUserId = userDetails.getUserId();
        MyProfileResponseDto responseDto = myPageService.getMyProfile(currentUserId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<MyProfileResponseDto> updateMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ProfileUpdateRequestDto requestDto
    ) {
        Long currentUserId = userDetails.getUserId();
        MyProfileResponseDto responseDto = myPageService.updateMyProfile(currentUserId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/blocks")
    public ResponseEntity<List<BlockResponseDto>> getMyBlocks(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "ALL") String type
    ) {
        Long currentUserId = userDetails.getUserId();
        List<BlockResponseDto> blocks = myPageService.getMyBlocks(currentUserId, type);
        return ResponseEntity.ok(blocks);
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam String type
    ) {
        Long currentUserId = userDetails.getUserId();
        List<ReviewResponseDto> reviews = myPageService.getMyReviews(currentUserId, type);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file
    ) {
        String fileUrl = myPageService.uploadFile(userDetails.getUserId(), file);
        return ResponseEntity.ok(Map.of("fileUrl", fileUrl));
    }
}