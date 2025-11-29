package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.Board;
import com.assemblock.assemblock_be.Entity.BoardBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardBlockRepository extends JpaRepository<BoardBlock, Long> {
    List<BoardBlock> findAllByBoard(Board board);
    boolean existsByBoardAndBlock(Board board, Block block);
    long countByBoard(Board board);
    List<BoardBlock> findTop4ByBoardOrderByCreatedAtDesc(Board board);
    void deleteByBoardAndBlockIn(Board board, List<Block> blocks);
}
