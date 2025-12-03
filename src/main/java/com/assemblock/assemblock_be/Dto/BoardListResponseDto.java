package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardListResponseDto {
    private Long boardId;
    private String boardName;
    private String boardMemo;
    private LocalDateTime createdAt;

    private int blockCount;
    private List<String> previewTypes;

    public BoardListResponseDto(Board board, int blockCount, List<String> previewTypes) {
        this.boardId = board.getId();
        this.boardName = board.getBoardName();
        this.boardMemo = board.getBoardMemo();
        this.createdAt = board.getCreatedAt();
        this.blockCount = blockCount;
        this.previewTypes = previewTypes;
    }
}