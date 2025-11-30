package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BoardDto;
import com.assemblock.assemblock_be.Dto.BoardListResponse;
import com.assemblock.assemblock_be.Service.BoardService;
import com.assemblock.assemblock_be.Entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<BoardListResponse>> getMyBoards(
            @AuthenticationPrincipal User user
    ) {
        Long userId = user.getId();
        List<BoardListResponse> boards = boardService.getBoardList(userId);
        return ResponseEntity.ok(boards);
    }

    @PostMapping
    public ResponseEntity<Void> createBoard(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody BoardDto request
    ) {
        Long boardId = boardService.createBoard(user.getId(), request);
        // 생성된 리소스 URI 반환 (201 Created)
        return ResponseEntity.created(URI.create("/api/boards/" + boardId)).build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto.BoardDetailResponse> getBoardDetails(
            @PathVariable Long boardId
    ) {
        BoardDto.BoardDetailResponse boardDetail = boardService.getBoardDetail(boardId);
        return ResponseEntity.ok(boardDetail);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<Void> updateBoard(
            @AuthenticationPrincipal User user,
            @PathVariable Long boardId,
            @Valid @RequestBody BoardDto request
    ) {
        boardService.updateBoard(user.getId(), boardId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @AuthenticationPrincipal User user,
            @PathVariable Long boardId
    ) {
        boardService.deleteBoard(user.getId(), boardId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PostMapping("/{boardId}/blocks")
    public ResponseEntity<Void> addBlockToBoard(
            @AuthenticationPrincipal User user,
            @PathVariable Long boardId,
            @RequestBody Map<String, Long> requestBody
    ) {
        Long blockId = requestBody.get("blockId");
        if (blockId == null) {
            throw new IllegalArgumentException("blockId가 요청에 포함되어야 합니다.");
        }
        boardService.addBlockToBoard(user.getId(), boardId, blockId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // --- 7. 블록 제거 (DELETE /api/v1/boards/{boardId}/blocks/{blockId}) ---
    @DeleteMapping("/{boardId}/blocks/{blockId}")
    public ResponseEntity<Void> removeBlockFromBoard(
            @AuthenticationPrincipal User user,
            @PathVariable Long boardId,
            @PathVariable Long blockId
    ) {
        boardService.removeBlockFromBoard(user.getId(), boardId, blockId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    }
}