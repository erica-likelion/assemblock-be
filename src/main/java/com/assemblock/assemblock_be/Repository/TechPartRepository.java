package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.TechPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechPartRepository extends JpaRepository<TechPart, Long> {
    Optional<TechPart> findByTechName(TechPart.TechName techName);
}