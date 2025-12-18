package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.Block;
import com.assemblock.assemblock_be.Entity.Block.BlockCategory;
import com.assemblock.assemblock_be.Entity.Block.TechPart;
import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {

    Optional<Block> findByUserAndBlockTitle(User user, String blockTitle);

    Page<Block> findAllByCategoryNameAndTechPartAndBlockTitleContaining(
            BlockCategory categoryName,
            TechPart techPart,
            String blockTitleKeyword,
            Pageable pageable
    );

    Page<Block> findAllByBlockTitleContaining(String blockTitleKeyword, Pageable pageable);

}