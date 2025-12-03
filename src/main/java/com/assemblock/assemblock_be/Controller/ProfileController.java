package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.MyProfileResponseDto;
import com.assemblock.assemblock_be.Dto.ReviewResponseDto;
import com.assemblock.assemblock_be.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/{userId}")
    public ResponseEntity<MyProfileResponseDto> getPublicProfile(
            @PathVariable Long userId
    ) {
        MyProfileResponseDto responseDto = profileService.getPublicProfile(userId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{userId}/blocks")
    public ResponseEntity<List<BlockResponseDto>> getPublicBlocks(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "ALL") String type
    ) {
        List<BlockResponseDto> blocks = profileService.getPublicBlocks(userId, type);
        return ResponseEntity.ok(blocks);
    }

    @GetMapping("/{userId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getPublicReviews(
            @PathVariable Long userId,
            @RequestParam String type
    ) {
        List<ReviewResponseDto> reviews = profileService.getPublicReviews(userId, type);
        return ResponseEntity.ok(reviews);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Not Found",
                        "message", e.getMessage()
                ));
    }
}