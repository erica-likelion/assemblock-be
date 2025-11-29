package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {
    private final BlockRepository blockRepository;

    public List<BlockResponseDto> searchBlocks(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }

        List<Block> blocks = blockRepository.findByTitleContainingWithUser(keyword);

        return blocks.stream()
                .map(block -> BlockResponseDto.builder()
                        .blockId(block.getId())
                        .title(block.getTitle())
                        .description(block.getOnelineSummary())
                        .username(block.getUser().getNickname())
                        .coverImageUrl(block.getResultUrl())
                        .build())
                .collect(Collectors.toList());
    }
}