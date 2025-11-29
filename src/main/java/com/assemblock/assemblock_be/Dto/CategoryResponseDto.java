package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.TechPart;
import lombok.Getter;
import java.util.List;

@Getter
public class CategoryResponseDto {
    private Long categoryId;
    private String categoryName;
    private List<SubCategoryDto> subCategories;

    public CategoryResponseDto(TechPart category, List<SubCategoryDto> subCategories) {
        this.categoryId = category.getId();
        this.categoryName = category.getTechName().name();
        this.subCategories = subCategories;
    }
}