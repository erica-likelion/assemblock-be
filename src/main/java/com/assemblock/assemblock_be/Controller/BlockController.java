package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockDto;
import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.BlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    /**
     * 1. 블록 생성 (POST)
     */
    @PostMapping
    public ResponseEntity<BlockResponseDto> createBlock(
            @Valid @RequestBody BlockDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        Long blockId = blockService.createBlock(user.getId(), requestDto);
        BlockResponseDto responseDto = blockService.getBlockDetail(blockId);
        return ResponseEntity.created(URI.create("/api/blocks/" + blockId)).body(responseDto);
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
     * 5. [수정됨] 블록 목록 조회 (GET)
     * - 기존 getBlockList 삭제함 (충돌 해결)
     * - blockType 파라미터로 필터링 가능
     */
    @GetMapping
    public ResponseEntity<List<BlockResponseDto>> findBlocks(
            @RequestParam(required = false) String blockType,
            @RequestParam(required = false) Block.BlockCategory category,
            @RequestParam(required = false) Block.TechPart techPart,
            @RequestParam(required = false) String keyword
    ) {
        List<BlockResponseDto> response = blockService.findBlocks(blockType, category, techPart, keyword);
        return ResponseEntity.ok(response);
    }
}