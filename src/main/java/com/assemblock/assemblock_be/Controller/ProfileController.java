package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.MyProfileResponseDto;
import com.assemblock.assemblock_be.Dto.ReviewResponseDto;
import com.assemblock.assemblock_be.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService profileService;

    /**
     * 타인 프로필 정보 조회
     * [GET] /api/profiles/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<MyProfileResponseDto> getPublicProfile(
            @PathVariable Long userId
    ) {
        MyProfileResponseDto responseDto = profileService.getPublicProfile(userId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 타인의 어셈블록 목록 조회
     * [GET] /api/profiles/{userId}/blocks?type={blockType}
     * (type: "ALL", "IDEA", "TECHNOLOGY")
     */
    @GetMapping("/{userId}/blocks")
    public ResponseEntity<List<BlockResponseDto>> getPublicBlocks(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "ALL") String type
    ) {
        List<BlockResponseDto> blocks = profileService.getPublicBlocks(userId, type);
        return ResponseEntity.ok(blocks);
    }

    /**
     * 타인의 후기블록 목록 조회
     * [GET] /api/profiles/{userId}/reviews?type={reviewType}
     * (type: "SCOUTING", "PARTICIPATION")
     */
    @GetMapping("/{userId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getPublicReviews(
            @PathVariable Long userId,
            @RequestParam String type
    ) {
        List<ReviewResponseDto> reviews = profileService.getPublicReviews(userId, type);
        return ResponseEntity.ok(reviews);
    }
}