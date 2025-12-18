package com.assemblock.assemblock_be.controller;

import com.assemblock.assemblock_be.dto.block.BlockCreateRequestDto;
import com.assemblock.assemblock_be.dto.block.BlockResponseDto;
import com.assemblock.assemblock_be.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blocks")
public class BlockController {

    private final BlockService blockService;

    // POST /api/blocks
    @PostMapping
    public BlockResponseDto create(@RequestBody BlockCreateRequestDto dto) {
        return blockService.create(dto);
    }

    // GET /api/blocks/{blockId}
    @GetMapping("/{blockId}")
    public BlockResponseDto get(@PathVariable Long blockId) {
        return blockService.findById(blockId);
    }
}
