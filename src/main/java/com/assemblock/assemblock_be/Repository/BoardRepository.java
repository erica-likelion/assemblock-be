package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.Board;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByUser(User user);
    Optional<Board> findByIdAndUser(Long boardId, User user);
    boolean existsByUserAndBoardName(User user, String boardName);
}