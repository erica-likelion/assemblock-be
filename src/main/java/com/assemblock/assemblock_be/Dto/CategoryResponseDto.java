package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.TechPart;
import lombok.Getter;
import java.util.List;

@Getter
public class CategoryResponseDto {
    private Long techId;
    private String techName;
    private List<SubCategoryDto> subCategories;

    public CategoryResponseDto(TechPart techPart, List<SubCategoryDto> subCategories) {
        this.techId = techPart.getId();
        this.techName = techPart.getTechName().name();
        this.subCategories = subCategories;
    }
}