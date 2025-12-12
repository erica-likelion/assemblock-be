package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    /*
    private final SearchService searchService;

    @GetMapping("/blocks")
    public ResponseEntity<List<BlockResponseDto>> searchBlocksByKeyword(
            @AuthenticationPrincipal User user,
            @RequestParam("q") String keyword
    ) {
        Long userId = user.getId();
        List<BlockResponseDto> result = searchService.searchBlocks(userId, keyword);
        return ResponseEntity.ok(result);
    }

     */

}
