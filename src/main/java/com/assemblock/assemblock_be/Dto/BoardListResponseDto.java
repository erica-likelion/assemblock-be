package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardListResponseDto {
    private Long boardId;
    private String boardName;
    private String boardMemo;
    private LocalDateTime createdAt;

    private int blockCount;

    public BoardListResponseDto(Board board, int blockCount) {
        this.boardId = board.getId();
        this.boardName = board.getBoardName();
        this.boardMemo = board.getBoardMemo();
        this.createdAt = board.getCreatedAt();
        this.blockCount = blockCount;
    }
}