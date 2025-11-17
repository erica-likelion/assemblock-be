package com.assemblock.assemblock_be.repository;

import java.util.List;

import com.assemblock.assemblock_be.entity.Review;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProject_ProjectId(Long projectId);
}

