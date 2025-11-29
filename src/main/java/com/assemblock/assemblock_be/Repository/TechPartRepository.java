package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.TechPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechPartRepository extends JpaRepository<TechPart, Long> {
}