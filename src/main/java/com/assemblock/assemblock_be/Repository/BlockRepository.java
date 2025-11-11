package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findByUser(User user);
    List<Block> findByCategoryName(BlockCategoryName categoryName);
    List<Block> findByTechParts(TechPart techPart);
    List<Block> findByUserAndBlockType(User user, BlockType blockType);

    @Query("SELECT b FROM Block b JOIN FETCH b.user WHERE b.title LIKE %:keyword%")
    List<Block> findByTitleContainingWithUser(@Param("keyword") String keyword);
}