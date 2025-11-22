package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.BlockCategoryName;
import com.assemblock.assemblock_be.Entity.BlockType;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findAllByUser(User user);
    List<Block> findAllByCategoryNameOrderByCreatedAtDesc(BlockCategoryName categoryName);
    List<Block> findAllByBlockTypeOrderByCreatedAtDesc(BlockType blockType);
    List<Block> findAllByUserAndBlockType(User user, BlockType blockType);
    @Query("SELECT b FROM Block b JOIN FETCH b.user " +
            "WHERE b.title LIKE %:keyword% OR b.onelineSummary LIKE %:keyword% " +
            "ORDER BY b.createdAt DESC")
    List<Block> findByKeyword(@Param("keyword") String keyword);
    List<Block> findAllByTechPart(Block.TechPart techPart);
}