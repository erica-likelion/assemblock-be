package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

// 유저의 보드 목록 조회 시 사용
@Getter
public class BoardListResponse {
    private Long boardId;
    private String boardName;
    private String boardMemo;
    private LocalDateTime createdAt;

    // 보드에 담긴 블록 개수 (Service에서 계산하여 설정)
    private int blockCount;

    public BoardListResponse(Board board, int blockCount) {
        this.boardId = board.getId();
        this.boardName = board.getBoardName();
        this.boardMemo = board.getBoardMemo();
        this.createdAt = board.getCreatedAt();
        this.blockCount = blockCount;
    }
}