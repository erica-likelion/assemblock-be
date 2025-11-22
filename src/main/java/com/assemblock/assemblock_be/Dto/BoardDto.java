package com.assemblock.assemblock_be.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

public class BoardDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardCreateRequest {
        @NotBlank @Size(max = 14)
        private String boardName;
        @Size(max = 104)
        private String boardMemo;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardUpdateRequest {
        @NotBlank @Size(max = 14)
        private String boardName;
        @Size(max = 104)
        private String boardMemo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardSummaryResponse {
        private Long boardId;
        private String boardName;
        private int blockCount;
        private List<String> previewTypes;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardDetailResponse {
        private Long boardId;
        private String boardName;
        private String boardMemo;
        private List<BlockResponseDto> blocks;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamProposalRequest {
        @NotBlank @Size(max = 14)
        private String projectTitle;

        @Size(max = 200)
        private String memo;

        @NotBlank @Size(max = 100)
        private String contact;

        @NotBlank
        private String recruitingPeriod;

        private List<Long> targetBlockIds;
    }
}