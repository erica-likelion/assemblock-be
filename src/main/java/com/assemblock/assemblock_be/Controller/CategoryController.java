package com.assemblock.assemblock_be.Controller;

import com.assemblock.assemblock_be.Dto.BlockResponse;
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

    @GetMapping("/grouped")
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        List<CategoryResponseDto> result = categoryService.getCategoriesWithTools();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/blocks")
    public ResponseEntity<List<BlockResponse>> getBlocks(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String type
    ) {
        List<BlockResponse> result = categoryService.getBlocks(categoryName, type);
        return ResponseEntity.ok(result);
    }
}