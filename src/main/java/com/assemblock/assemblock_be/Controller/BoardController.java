// 카카오로그인 구현 후 수정

package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BoardDto;
import com.assemblock.assemblock_be.Service.BoardService;
import com.assemblock.assemblock_be.Security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<BoardDto.BoardSummaryResponse>> getMyBoards(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUserId();
        List<BoardDto.BoardSummaryResponse> boards = boardService.getMyBoards(userId);
        return ResponseEntity.ok(boards);
    }

    @PostMapping
    public ResponseEntity<BoardDto.BoardDetailResponse> createBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody BoardDto.BoardCreateRequest requestDto
    ) {
        Long userId = userDetails.getUserId();
        BoardDto.BoardDetailResponse createdBoard = boardService.createBoard(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto.BoardDetailResponse> getBoardDetails(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId
    ) {
        Long userId = userDetails.getUserId();
        BoardDto.BoardDetailResponse boardDetail = boardService.getBoardDetails(userId, boardId);
        return ResponseEntity.ok(boardDetail);
    }

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

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId
    ) {
        Long userId = userDetails.getUserId();
        boardService.deleteBoard(userId, boardId);
        return ResponseEntity.noContent().build();
    }

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

    @DeleteMapping("/{boardId}/blocks")
    public ResponseEntity<Void> removeBlocksFromBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId,
            @RequestParam List<Long> blockIds
    ) {
        Long userId = userDetails.getUserId();
        boardService.removeBlocksFromBoard(userId, boardId, blockIds);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/proposals")
    public ResponseEntity<Void> createTeamProposal(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody BoardDto.TeamProposalRequest requestDto
    ) {
        Long userId = userDetails.getUserId();
        boardService.createTeamProposal(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

