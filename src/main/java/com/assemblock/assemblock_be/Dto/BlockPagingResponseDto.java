package com.assemblock.assemblock_be.Dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class BlockPagingResponseDto<T> {
    private final List<T> content;
    private final int totalPages;
    private final long totalElements;
    private final int pageSize;
    private final int pageNumber;
    private final boolean isLast;

    public BlockPagingResponseDto(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageSize = page.getSize();
        this.pageNumber = page.getNumber() + 1;
        this.isLast = page.isLast();
    }
}