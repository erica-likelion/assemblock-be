package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.SearchDto;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/blocks")
    public ResponseEntity<List<BlockResponseDto>> searchBlocksByKeyword(
            @AuthenticationPrincipal User user,
            @RequestParam("q") String keyword
    ) {
        Long userId = user.getUser();
        List<BlockResponseDto> result = searchService.searchBlocks(userId, keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    public ResponseEntity<List<SearchDto.SearchHistoryResponse>> getSearchHistory(
            @AuthenticationPrincipal User user
    ) {
        Long userId = user.getUser();
        List<SearchDto.SearchHistoryResponse> history = searchService.getSearchHistory(userId);
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> deleteAllSearchHistory(
            @AuthenticationPrincipal User user
    ) {
        Long userId = user.getUser();
        searchService.deleteAllSearchHistory(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/history/{historyId}")
    public ResponseEntity<Void> deleteSearchHistoryItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long historyId
    ) {
        Long userId = user.getUser();
        searchService.deleteSearchHistoryItem(userId, historyId);
        return ResponseEntity.noContent().build();
    }
}
