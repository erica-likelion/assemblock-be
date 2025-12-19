package com.assemblock.assemblock_be.Repository;

import java.util.List;
import com.assemblock.assemblock_be.Entity.Review;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProject_Id(Long projectId);

    List<Review> findAllByUser(User user);

    List<Review> findAllByReviewedUser(User reviewedUser);
}