// 카카오 로그인 구현 후 수정

package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.SearchDto;
import com.assemblock.assemblock_be.Service.SearchService;
import com.assemblock.assemblock_be.Security.UserDetailsImpl;
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
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("q") String keyword
    ) {
        Long userId = userDetails.getUserId();
        List<BlockResponseDto> result = searchService.searchBlocks(userId, keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    public ResponseEntity<List<SearchDto.SearchHistoryResponse>> getSearchHistory(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUserId();
        List<SearchDto.SearchHistoryResponse> history = searchService.getSearchHistory(userId);
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> deleteAllSearchHistory(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUserId();
        searchService.deleteAllSearchHistory(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/history/{historyId}")
    public ResponseEntity<Void> deleteSearchHistoryItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long historyId
    ) {
        Long userId = userDetails.getUserId();
        searchService.deleteSearchHistoryItem(userId, historyId);
        return ResponseEntity.noContent().build();
    }
}
