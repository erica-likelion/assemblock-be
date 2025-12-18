package com.assemblock.assemblock_be.repository;

import com.assemblock.assemblock_be.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, Long> {
}