package com.assemblock.assemblock_be.Dto;

import com.assemblock.assemblock_be.Entity.Block;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockDto {

    @NotBlank(message = "블록 제목은 필수입니다.")
    private String blockTitle;

    @NotNull(message = "카테고리는 필수입니다.")
    private Block.BlockCategory categoryName;

    private Block.TechPart techPart;

    @NotNull(message = "블록 타입은 필수입니다.")
    private Block.BlockType blockType;

    @NotNull(message = "기여도는 필수입니다.")
    @Min(0) @Max(100)
    private Integer contributionScore;

    @NotBlank(message = "툴 설명은 필수입니다.")
    private String toolsText;

    @NotBlank(message = "한줄 요약은 필수입니다.")
    private String oneLineSummary;

    private String improvementPoint;

    private String resultUrl;

    private String resultFile;
}