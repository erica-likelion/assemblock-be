package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Check;
import com.assemblock.assemblock_be.Dto.BlockDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "Block")
public class Block extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id",  nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "tech_part")
    private TechPart techPart;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_name")
    private BlockCategory categoryName;

    @Column(name = "block_title", nullable = false)
    private String blockTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "block_type", nullable = false)
    private BlockType blockType;

    @Check(constraints = "contribution_score >= 0 AND contribution_score <= 100")
    @Column(name = "contribution_score", nullable = false)
    private @NotNull(message = "기여도는 필수입니다.")
    @Min(0)
    @Max(100) Integer contributionScore;

    @Column(name = "tools_text", columnDefinition = "TEXT")
    private String toolsText;

    @Column(name = "oneline_summary", columnDefinition = "TEXT", nullable = false)
    private String oneLineSummary;

    @Column(name = "improvement_point", columnDefinition = "TEXT")
    private String improvementPoint;

    @Column(name = "result_url")
    private String resultUrl;

    @Column(name = "result_file", length = 2048)
    private String resultFile;




    public enum BlockCategory {
        데이터_시각화, API_연동, 레이아웃_그리드, 인터랙션_애니메이션, 상태_관리, 성능_최적화, // back-end
        백엔드, AI_기능_활용,
        UXUI디자인, 비주얼_그래픽_디자인, 브랜드_디자인, 아이콘_디자인, 인터랙션_및_모션_디자인, // front-end
        경제_금융, 환경_지속가능성, 교육_학습, 주거_공간, 의료_건강, 문화_생활, 관계_심리, // idea
        육아_살림, 사회_커뮤니티, 기술_AI, 기타
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
        this.improvementPoint = dto.getImprovementPoint();
        this.resultUrl = dto.getResultUrl();
        this.resultFile = dto.getResultFile();
    }

    public void update(BlockDto dto) {
        this.techPart = dto.getTechPart();
        this.categoryName = dto.getCategoryName();
        this.blockTitle = dto.getBlockTitle();
        this.blockType = dto.getBlockType();
        this.contributionScore = dto.getContributionScore();
        this.toolsText = dto.getToolsText();
        this.oneLineSummary = dto.getOneLineSummary();
        this.improvementPoint = dto.getImprovementPoint();
        this.resultUrl = dto.getResultUrl();
        this.resultFile = dto.getResultFile();
    }
}