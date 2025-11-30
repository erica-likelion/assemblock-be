// 카카오로그인 구현 후 수정

package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.NotificationResponseDto;
import com.assemblock.assemblock_be.Service.NotificationService;
import com.assemblock.assemblock_be.Security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getMyNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long currentUserId = userDetails.getUserId();
        List<NotificationResponseDto> notifications = notificationService.getPendingNotifications(currentUserId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/{proposalId}/accept")
    public ResponseEntity<Void> acceptNotification(
            @PathVariable Long proposalId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        Long currentUserId = userDetails.getUserId();
        notificationService.acceptProposal(currentUserId, proposalId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{proposalId}/reject")
    public ResponseEntity<Void> rejectNotification(
            @PathVariable Long proposalId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        Long currentUserId = userDetails.getUserId();
        notificationService.rejectProposal(currentUserId, proposalId);
        return ResponseEntity.ok().build();
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