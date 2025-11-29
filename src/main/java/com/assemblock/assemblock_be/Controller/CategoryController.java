package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockResponseDto;
import com.assemblock.assemblock_be.Dto.CategoryResponseDto;
import com.assemblock.assemblock_be.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * API: 카테고리 및 하위 툴 그룹 조회
     * [GET] /api/categories/grouped
     */
    @GetMapping("/grouped")
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        List<CategoryResponseDto> result = categoryService.getCategoriesWithTools();
        return ResponseEntity.ok(result);
    }

    /**
     * API: 특정 카테고리에 속한 블록 목록 조회
     * [GET] /api/categories/blocks
     */
    @GetMapping("/blocks")
    public ResponseEntity<List<BlockResponseDto>> getBlocksByCategoryName(
            @RequestParam String categoryName
    ) {
        List<BlockResponseDto> result = categoryService.getBlocksByCategoryName(categoryName);
        return ResponseEntity.ok(result);
    }
}