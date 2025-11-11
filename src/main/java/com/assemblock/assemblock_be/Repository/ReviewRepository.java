package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.Review;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUser(User user);
    List<Review> findByReviewer(User reviewer);
}