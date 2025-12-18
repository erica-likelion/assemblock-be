package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.Block.BlockCategory;
import com.assemblock.assemblock_be.Entity.Block.TechPart;
import com.assemblock.assemblock_be.Entity.Block.BlockType;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {

    @Override
    @EntityGraph(attributePaths = {"user"})
    List<Block> findAll();

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT b FROM Block b WHERE " +
            "(:blockType IS NULL OR b.blockType = :blockType) AND " + // 블록 타입 추가
            "(:category IS NULL OR b.categoryName = :category) AND " +
            "(:techPart IS NULL OR b.techPart = :techPart) AND " +
            "(:keyword IS NULL OR (b.blockTitle LIKE CONCAT('%', :keyword, '%') OR b.oneLineSummary LIKE CONCAT('%', :keyword, '%'))) " + // CONCAT으로 수정
            "ORDER BY b.createdAt DESC")
    List<Block> findBlocksDynamic(
            @Param("blockType") BlockType blockType,
            @Param("category") BlockCategory category,
            @Param("techPart") TechPart techPart,
            @Param("keyword") String keyword
    );

    @EntityGraph(attributePaths = {"user"})
    List<Block> findAllByUser(User user);

    @EntityGraph(attributePaths = {"user"})
    List<Block> findAllByUserAndBlockType(User user, Block.BlockType blockType);

    @EntityGraph(attributePaths = {"user"})
    List<Block> findAllByBlockType(Block.BlockType blockType);

}