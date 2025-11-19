// 로그인 구현 후 수정 필요

package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BoardDto;
import com.assemblock.assemblock_be.Service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal; // 카카오로그인 구현 후 수정
import com.assemblock.assemblock_be.security.UserDetailsImpl; // 카카오로그인 구현 후 수정

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    /**
     * 내 보드 목록 조회
     * [GET] /api/boards
     */
    @GetMapping
    public ResponseEntity<List<BoardDto.BoardSummaryResponse>> getMyBoards(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUserId();
        List<BoardDto.BoardSummaryResponse> boards = boardService.getMyBoards(userId);
        return ResponseEntity.ok(boards);
    }

    /**
     * 새 보드 생성
     * [POST] /api/boards
     */
    @PostMapping
    public ResponseEntity<BoardDto.BoardDetailResponse> createBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody BoardDto.BoardCreateRequest requestDto
    ) {
        Long userId = userDetails.getUserId();
        BoardDto.BoardDetailResponse createdBoard = boardService.createBoard(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }

    /**
     * 특정 보드 상세 조회
     * [GET] /api/boards/{boardId}
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto.BoardDetailResponse> getBoardDetails(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId
    ) {
        Long userId = userDetails.getUserId();
        BoardDto.BoardDetailResponse boardDetail = boardService.getBoardDetails(userId, boardId);
        return ResponseEntity.ok(boardDetail);
    }

    /**
     * 보드 정보 수정
     * [PUT] /api/boards/{boardId}
     */
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardDto.BoardDetailResponse> updateBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId,
            @RequestBody BoardDto.BoardUpdateRequest requestDto
    ) {
        Long userId = userDetails.getUserId();
        BoardDto.BoardDetailResponse updatedBoard = boardService.updateBoard(userId, boardId, requestDto);
        return ResponseEntity.ok(updatedBoard);
    }

    /**
     * 보드 삭제
     * [DELETE] /api/boards/{boardId}
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId
    ) {
        Long userId = userDetails.getUserId();
        boardService.deleteBoard(userId, boardId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 보드에 블록 추가
     * [POST] /api/boards/{boardId}/blocks
     */
    @PostMapping("/{boardId}/blocks")
    public ResponseEntity<Void> addBlockToBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId,
            @RequestBody Map<String, Long> requestBody
    ) {
        Long userId = userDetails.getUserId();
        Long blockId = requestBody.get("blockId");
        boardService.addBlockToBoard(userId, boardId, blockId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 보드에서 블록 제거
     * [DELETE] /api/boards/{boardId}/blocks/{blockId}
     */
    @DeleteMapping("/{boardId}/blocks/{blockId}")
    public ResponseEntity<Void> removeBlockFromBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId,
            @PathVariable Long blockId
    ) {
        Long userId = userDetails.getUserId();
        boardService.removeBlockFromBoard(userId, boardId, blockId);
        return ResponseEntity.noContent().build();
    }
}