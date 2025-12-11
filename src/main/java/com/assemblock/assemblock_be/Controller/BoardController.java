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

    @GetMapping
    public ResponseEntity<List<BoardListResponseDto>> getMyBoards(
            @AuthenticationPrincipal User user
    ) {
        List<BoardListResponseDto> boards = boardService.getMyBoards(user.getId());
        return ResponseEntity.ok(boards);
    }

    @PostMapping
    public ResponseEntity<BoardDto.BoardDetailResponse> createBoard(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody BoardDto.BoardCreateRequest request
    ) {
        BoardDto.BoardDetailResponse response = boardService.createBoard(user.getId(), request);
        return ResponseEntity.created(URI.create("/api/boards/" + response.getBoardId())).body(response);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto.BoardDetailResponse> getBoardDetails(
            @AuthenticationPrincipal User user,
            @PathVariable Long boardId
    ) {
        BoardDto.BoardDetailResponse boardDetail = boardService.getBoardDetails(user.getId(), boardId);
        return ResponseEntity.ok(boardDetail);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardDto.BoardDetailResponse> updateBoard(
            @AuthenticationPrincipal User user,
            @PathVariable Long boardId,
            @Valid @RequestBody BoardDto.BoardUpdateRequest request
    ) {
        boardService.updateBoard(user.getId(), boardId, request);
        BoardDto.BoardDetailResponse updatedBoard = boardService.getBoardDetails(user.getId(), boardId);
        return ResponseEntity.ok(updatedBoard);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @AuthenticationPrincipal User user,
            @PathVariable Long boardId
    ) {
        boardService.deleteBoard(user.getId(), boardId);
        return ResponseEntity.noContent().build();
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

    @DeleteMapping("/{boardId}/blocks/{blockId}")
    public ResponseEntity<Void> removeBlockFromBoard(
            @AuthenticationPrincipal User user,
            @PathVariable Long boardId,
            @PathVariable Long blockId
    ) {
        boardService.removeBlocksFromBoard(user.getId(), boardId, Collections.singletonList(blockId));
        return ResponseEntity.noContent().build();
    }
/*
    @PostMapping("/proposals")
    public ResponseEntity<Void> createTeamProposal(
            @AuthenticationPrincipal User user,
            @RequestBody BoardDto.TeamProposalRequest requestDto
    ) {
        boardService.createTeamProposal(user.getId(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

*/
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