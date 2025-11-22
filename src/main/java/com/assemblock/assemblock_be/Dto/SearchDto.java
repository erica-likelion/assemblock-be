package com.assemblock.assemblock_be.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SearchDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchHistoryResponse {
        private Long historyId;
        private String keyword;
    }
}