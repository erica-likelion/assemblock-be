// reviewrepository와 projectmemberrepository 파일 확인 필요

package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findByUser(User user);
    List<Block> findByCategoryName(BlockCategoryName categoryName);
    List<Block> findByTechPart(TechPart techPart);
    List<Block> findByUserAndBlockType(User user, BlockType blockType);
    @Query("SELECT b FROM Block b JOIN FETCH b.user WHERE b.title LIKE %:keyword%")
    List<Block> findByBlockTitleContaining(@Param("keyword") String keyword);

    @Query("SELECT b FROM Block b " +
            "LEFT JOIN b.techPart tp " +
            "WHERE (:keyword IS NULL OR :keyword = '' OR b.title LIKE %:keyword%) " +
            "AND (:categoryName IS NULL OR b.categoryName = :categoryName) " +
            "AND (:techName IS NULL OR tp.techName = :techName)")
    List<Block> findBySearchCriteria(
            @Param("keyword") String keyword,
            @Param("categoryName") BlockCategoryName categoryName,
            @Param("techName") TechPart.TechName techName);
}