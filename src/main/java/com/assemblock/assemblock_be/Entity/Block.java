package com.assemblock.assemblock_be.Entity;

import com.assemblock.assemblock_be.Dto.BlockDto; // 수정: BlockRequestDto -> BlockDto
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "Block") // SQL DDL에 맞춰 테이블 이름을 'Block'으로 변경
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long id;

    // 작성자 (User 테이블의 user_id FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // tech_part (SQL DDL에 맞춰 단일 ENUM으로 매핑)
    @Enumerated(EnumType.STRING)
    @Column(name = "tech_part") // SQL DDL 필드명 사용
    private TechPart techPart;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_name")
    private BlockCategory categoryName;

    @Column(name = "block_title", nullable = false)
    private String blockTitle; // SQL: block_title

    @Enumerated(EnumType.STRING)
    @Column(name = "block_type", nullable = false)
    private BlockType blockType;

    @Column(name = "contribution_score", nullable = false)
    private Integer contributionScore; // SQL: TINYINT

    @Column(name = "tools_text", columnDefinition = "TEXT")
    private String toolsText;

    @Column(name = "oneline_summary", columnDefinition = "TEXT", nullable = false)
    private String oneLineSummary;


    public enum BlockCategory {
        데이터_시각화, API_연동, 레이아웃_그리드, 인터랙션_애니메이션, 상태_관리, 성능_최적화,
        백엔드, AI_기능_활용,
        UXUI디자인, 비주얼_그래픽_디자인, 브랜드_디자인, 아이콘_디자인, 인터랙션_및_모션_디자인
    }

    public enum TechPart {
        FRONTEND,
        BACKEND,
        DESIGN
    }

    public enum BlockType {
        TECHNOLOGY,
        IDEA
    }

    @Builder
    public Block(User user, BlockDto dto) {
        this.user = user;
        this.techPart = dto.getTechPart();
        this.categoryName = dto.getCategoryName();
        this.blockTitle = dto.getBlockTitle();
        this.blockType = dto.getBlockType();
        this.contributionScore = dto.getContributionScore();
        this.toolsText = dto.getToolsText();
        this.oneLineSummary = dto.getOneLineSummary();
    }

    // --- ▽ 수정 메서드 (Update) ▽ ---
    public void update(BlockDto dto) {
        this.techPart = dto.getTechPart();
        this.categoryName = dto.getCategoryName();
        this.blockTitle = dto.getBlockTitle();
        this.blockType = dto.getBlockType();
        this.contributionScore = dto.getContributionScore();
        this.toolsText = dto.getToolsText();
        this.oneLineSummary = dto.getOneLineSummary();
    }
}