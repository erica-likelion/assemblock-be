package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum BlockCategoryName {
    프론트엔드("프론트엔드"),
    데이터_시각화("데이터 시각화"),
    API_연동("API 연동"),
    레이아웃_그리드("레이아웃/그리드"),
    인터랙션_애니메이션("인터랙션/애니메이션"),
    상태_관리("상태 관리"),
    성능_최적화("성능 최적화"),
    백엔드("백엔드"),
    AI_기능_활용("AI 기능 활용"),
    디자인("디자인");

    private final String dbValue;
    BlockCategoryName(String dbValue) {
        this.dbValue = dbValue;
    }

    @Converter(autoApply = true)
    public static class BlockCategoryNameConverter implements AttributeConverter<BlockCategoryName, String> {
        @Override
        public String convertToDatabaseColumn(BlockCategoryName categoryName) {
            if (categoryName == null) {
                return null;
            }
            return categoryName.getDbValue();
        }

        @Override
        public BlockCategoryName convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }
            return Stream.of(BlockCategoryName.values())
                    .filter(c -> c.getDbValue().equals(dbData))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown DB value: " + dbData));
        }
    }
}