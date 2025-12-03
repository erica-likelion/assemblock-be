package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Block;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private String techPart;
    private List<String> categoryNames;

    public CategoryResponseDto(Block.TechPart techPart, List<String> categoryNames) {
        this.techPart = techPart.getValue();
        this.categoryNames = categoryNames;
    }
}