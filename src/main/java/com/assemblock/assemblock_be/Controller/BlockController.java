package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockDto;
import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.BlockPagingResponseDto;
import com.assemblock.assemblock_be.Dto.BlockListResponseDto;
import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.BlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    /**
     * 1. 블록 생성 (POST)
     */
    @PostMapping
    public ResponseEntity<Void> createBlock(
            @Valid @RequestBody BlockDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        Long blockId = blockService.createBlock(user.getId(), requestDto);

        return ResponseEntity.created(URI.create("/api/blocks/" + blockId)).build();
    }

    /**
     * 2. 블록 상세 조회 (GET)
     */
    @GetMapping("/{blockId}")
    public ResponseEntity<BlockResponseDto> getBlockDetail(
                                                         @PathVariable Long blockId
    ) {
        BlockResponseDto responseDto = blockService.getBlockDetail(blockId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 3. 블록 수정 (PUT)
     */
    @PutMapping("/{blockId}")
    public ResponseEntity<Void> updateBlock(
            @PathVariable Long blockId,
            @Valid @RequestBody BlockDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        blockService.updateBlock(user.getId(), blockId, requestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 4. 블록 삭제 (DELETE)
     */
    @DeleteMapping("/{blockId}")
    public ResponseEntity<Void> deleteBlock(
            @PathVariable Long blockId,
            @AuthenticationPrincipal User user
    ) {
        blockService.deleteBlock(user.getId(), blockId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 5. 블록 목록 조회/검색 (GET) - 신규
     */
    @GetMapping
    public ResponseEntity<BlockPagingResponseDto<BlockListResponseDto>> getBlockList(
            @RequestParam(required = false) Optional<Block.BlockCategory> category,
            @RequestParam(required = false) Optional<Block.TechPart> techPart,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        BlockPagingResponseDto<BlockListResponseDto> response = blockService.getBlockList(category, techPart, keyword, pageable);
        return ResponseEntity.ok(response);
    }
}