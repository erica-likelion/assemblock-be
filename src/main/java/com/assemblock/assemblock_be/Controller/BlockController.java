package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockDto;
import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Service.BlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BlockResponseDto> createBlock(
            @RequestPart(value = "dto") @Valid BlockDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal User user
    ) throws IOException {
        Long blockId = blockService.createBlock(user.getId(), requestDto, file);
        BlockResponseDto responseDto = blockService.getBlockDetail(blockId);
        return ResponseEntity.created(URI.create("/api/blocks/" + blockId)).body(responseDto);
    }

    @GetMapping("/{blockId}")
    public ResponseEntity<BlockResponseDto> getBlockDetail(
            @PathVariable Long blockId
    ) {
        BlockResponseDto responseDto = blockService.getBlockDetail(blockId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping(value = "/{blockId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateBlock(
            @PathVariable Long blockId,
            @RequestPart(value = "dto") @Valid BlockDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal User user
    ) throws IOException {
        blockService.updateBlock(user.getId(), blockId, requestDto, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{blockId}")
    public ResponseEntity<Void> deleteBlock(
            @PathVariable Long blockId,
            @AuthenticationPrincipal User user
    ) {
        blockService.deleteBlock(user.getId(), blockId);
        return ResponseEntity.noContent().build();
    }

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