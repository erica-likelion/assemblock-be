package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BoardDto;
import com.assemblock.assemblock_be.Dto.BoardListResponseDto;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    // 1. 내 보드 목록 조회
    @GetMapping
    public ResponseEntity<List<BoardListResponseDto>> getMyBoards(
            @AuthenticationPrincipal User user
    ) {
        List<BoardListResponseDto> boards = boardService.getMyBoards(user.getId());
        return ResponseEntity.ok(boards);
    }

    // 2. 보드 생성
    @PostMapping
    public ResponseEntity<BoardDto.BoardDetailResponse> createBoard(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody BoardDto.BoardCreateRequest request
    ) {
        BoardDto.BoardDetailResponse response = boardService.createBoard(user.getId(), request);
        return ResponseEntity.created(URI.create("/api/boards/" + response.getBoardId())).body(response);
    }

    // 3. 보드 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto.BoardDetailResponse> getBoardDetails(
            @AuthenticationPrincipal User user,
            @PathVariable Long boardId
    ) {
        BoardDto.BoardDetailResponse boardDetail = boardService.getBoardDetails(user.getId(), boardId);
        return ResponseEntity.ok(boardDetail);
    }

    // 4. 보드 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<Void> updateBoard(
            @AuthenticationPrincipal User user,
            @PathVariable Long boardId,
            @Valid @RequestBody BoardDto.BoardUpdateRequest request
    ) {
        boardService.updateBoard(user.getId(), boardId, request);
        return ResponseEntity.ok().build();
    }

    // 5. 보드 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @AuthenticationPrincipal User user,
            @PathVariable Long boardId
    ) {
        boardService.deleteBoard(user.getId(), boardId);
        return ResponseEntity.noContent().build();
    }

    // 6. 보드에 블록 추가
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

    // 7. 보드에서 블록 제거
    @DeleteMapping("/{boardId}/blocks/{blockId}")
    public ResponseEntity<Void> removeBlockFromBoard(
            @AuthenticationPrincipal User user,
            @PathVariable Long boardId,
            @PathVariable Long blockId
    ) {
        boardService.removeBlocksFromBoard(user.getId(), boardId, Collections.singletonList(blockId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/proposals")
    public ResponseEntity<Void> createTeamProposal(
            @AuthenticationPrincipal User user,
            @RequestBody BoardDto.TeamProposalRequest requestDto
    ) {
        boardService.createTeamProposal(user.getId(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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