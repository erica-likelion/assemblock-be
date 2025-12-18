package com.assemblock.assemblock_be.dto.block;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockCreateRequestDto {

    private String blockTitle;
    private String blockType;
    private int contributionScore;
    private String onelineSummary;
    private String resultUrl;
    private String resultFile;
    private String resultFileName; 
}
