package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.SearchDto;
import com.assemblock.assemblock_be.Service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    /**
     * 다양한 조건으로 블록 검색
     * [GET] /api/search/blocks?keyword=
     */
    @GetMapping("/blocks")
    public ResponseEntity<List<BlockResponseDto>> searchBlocks(
            @ModelAttribute SearchDto searchDto
    ) {
        List<BlockResponseDto> result = searchService.searchBlocks(searchDto);
        return ResponseEntity.ok(result);
    }
}