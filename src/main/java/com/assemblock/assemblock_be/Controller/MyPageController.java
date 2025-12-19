package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.MyProfileResponseDto;
import com.assemblock.assemblock_be.Dto.ProfileUpdateRequestDto;
import com.assemblock.assemblock_be.Dto.ReviewResponseDto;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @PutMapping("/profile")
    public ResponseEntity<MyProfileResponseDto> updateMyProfile(
            @AuthenticationPrincipal User user,
            @RequestBody ProfileUpdateRequestDto requestDto
    ) {
        Long currentUserId = user.getId();
        MyProfileResponseDto responseDto = myPageService.updateMyProfile(currentUserId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/blocks")
    public ResponseEntity<List<BlockResponseDto>> getMyBlocks(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "ALL") String type
    ) {
        Long currentUserId = user.getId();
        List<BlockResponseDto> blocks = myPageService.getMyBlocks(currentUserId, type);
        return ResponseEntity.ok(blocks);
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(
            @AuthenticationPrincipal User user,
            @RequestParam String type
    ) {
        Long currentUserId = user.getId();
        List<ReviewResponseDto> reviews = myPageService.getMyReviews(currentUserId, type);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file
    ) {
        Long currentUserId = user.getId();
        String fileUrl = myPageService.uploadFile(currentUserId, file);
        return ResponseEntity.ok(Map.of("fileUrl", fileUrl));
    }
}