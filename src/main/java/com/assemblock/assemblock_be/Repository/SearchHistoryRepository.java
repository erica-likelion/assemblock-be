package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.SearchHistory;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findAllByUserOrderByCreatedAtDesc(User user);
    void deleteByUser(User user);
    void deleteByIdAndUser(Long id, User user);
}
