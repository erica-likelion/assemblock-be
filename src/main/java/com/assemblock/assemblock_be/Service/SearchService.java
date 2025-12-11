package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.*;
import com.assemblock.assemblock_be.Entity.*;
import com.assemblock.assemblock_be.Repository.*;
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
    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<BlockResponseDto> searchBlocks(Long userId, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        searchHistoryRepository.save(SearchHistory.builder()
                .user(user)
                .keyword(keyword)
                .build());

        List<Block> blocks = blockRepository.findBlocksDynamic(null, null, keyword);

        return blocks.stream()
                .map(BlockResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
/*
    public List<SearchDto.SearchHistoryResponse> getSearchHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return searchHistoryRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream()
                .limit(10)
                .map(history -> SearchDto.SearchHistoryResponse.builder()
                        .historyId(history.getId())
                        .keyword(history.getKeyword())
                        .build())
                .collect(Collectors.toList());
    }
*/
    @Transactional
    public void deleteAllSearchHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        searchHistoryRepository.deleteByUser(user);
    }

    @Transactional
    public void deleteSearchHistoryItem(Long userId, Long historyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        searchHistoryRepository.deleteByIdAndUser(historyId, user);
    }
}
