package com.assemblock.assemblock_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class BoardDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardCreateRequest {
        private String boardName;
        private String boardMemo;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardUpdateRequest {
        private String boardName;
        private String boardMemo;
    }

    @Getter
    public static class BoardSummaryResponse {
        private Long boardId;
        private String boardName;
        private int blockCount;
        private List<String> previewImageUrls;

        @Builder
        public BoardSummaryResponse(Long boardId, String boardName, int blockCount, List<String> previewImageUrls) {
            this.boardId = boardId;
            this.boardName = boardName;
            this.blockCount = blockCount;
            this.previewImageUrls = previewImageUrls;
        }
    }

    @Getter
    public static class BoardDetailResponse {
        private Long boardId;
        private String boardName;
        private String boardMemo;
        private List<BlockResponseDto> blocks;

        @Builder
        public BoardDetailResponse(Long boardId, String boardName, String boardMemo, List<BlockResponseDto> blocks) {
            this.boardId = boardId;
            this.boardName = boardName;
            this.boardMemo = boardMemo;
            this.blocks = blocks;
        }
    }
}