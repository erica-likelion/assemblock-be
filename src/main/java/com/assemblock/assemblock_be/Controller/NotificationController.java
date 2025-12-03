package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.NotificationResponseDto;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getPendingNotifications(
            @AuthenticationPrincipal User user
    ) {
        List<NotificationResponseDto> notifications = notificationService.getPendingNotifications(user.getUser());
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/{proposalId}/accept")
    public ResponseEntity<Void> acceptProposal(
            @AuthenticationPrincipal User user,
            @PathVariable Long proposalId
    ) throws AccessDeniedException {
        notificationService.acceptProposal(user.getUser(), proposalId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{proposalId}/reject")
    public ResponseEntity<Void> rejectProposal(
            @AuthenticationPrincipal User user,
            @PathVariable Long proposalId
    ) throws AccessDeniedException {
        notificationService.rejectProposal(user.getUser(), proposalId);
        return ResponseEntity.ok().build();
    }
}