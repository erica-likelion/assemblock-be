package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Entity.TechPart;
import com.assemblock.assemblock_be.Entity.TechPart.TechName;
import com.assemblock.assemblock_be.Repository.TechPartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TechPartService {
    private final TechPartRepository techPartRepository;

    public TechPart findTechPartByEnum(TechName techName) {
        return techPartRepository.findByTechName(techName)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 기술 파트 이름입니다: " + techName));
    }
}