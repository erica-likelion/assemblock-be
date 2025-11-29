package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.NotificationResponseDto;
import com.assemblock.assemblock_be.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal; // 카카오로그인 구현 후 수정
import com.assemblock.assemblock_be.security.UserDetailsImpl; // 카카오로그인 구현 후 수정

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    /**
     * 내 알림 (받은 제안) 목록 조회
     * GET /api/notifications
     */
    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getMyNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails // 카카오로그인 구현 후 수정
    ) {
        Long currentUserId = userDetails.getUserId();
        List<NotificationResponseDto> notifications = notificationService.getPendingNotifications(currentUserId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * 제안 수락
     * POST /api/notifications/{proposalId}/accept
     */
    @PostMapping("/{proposalId}/accept")
    public ResponseEntity<Void> acceptNotification(
            @PathVariable Long proposalId,
            @AuthenticationPrincipal UserDetailsImpl userDetails // 카카오로그인 구현 후 수정
    ) {
        Long currentUserId = userDetails.getUserId();
        try {
            notificationService.acceptProposal(currentUserId, proposalId);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 (이미 처리됨)
        }
    }

    /**
     * 제안 거절
     * POST /api/notifications/{proposalId}/reject
     */
    @PostMapping("/{proposalId}/reject")
    public ResponseEntity<Void> rejectNotification(
            @PathVariable Long proposalId,
            @AuthenticationPrincipal UserDetailsImpl userDetails // 카카오로그인 구현 후 수정
    ) {
        Long currentUserId = userDetails.getUserId();
        try {
            notificationService.rejectProposal(currentUserId, proposalId);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    }
}