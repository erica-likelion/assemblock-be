package com.assemblock.assemblock_be.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TechPartUpdateRequestDto {
    private List<String> techNames;
}