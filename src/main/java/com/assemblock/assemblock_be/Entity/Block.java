package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Blocks")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Convert(converter = BlockCategoryName.BlockCategoryNameConverter.class)
    @Column(name = "category_name")
    private BlockCategoryName categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_id")
    private TechPart techPart;

    @Column(name = "block_title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "block_type", nullable = false)
    private BlockType blockType;

    @Check(constraints = "contribution_score >= 0 AND contribution_score <= 10")
    @Column(name = "contribution_score", nullable = false)
    private Byte contributionScore;

    @Column(name = "tools_text", columnDefinition = "TEXT")
    private String toolsText;

    @Column(name = "oneline_summary", nullable = false, columnDefinition = "TEXT")
    private String onelineSummary;

    @Column(name = "improvement_point", columnDefinition = "TEXT")
    private String improvementPoint;

    @Column(name = "result_url", length = 2048)
    private String resultUrl;

    @Column(name = "result_file", length = 2048)
    private String resultFile;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "proposalBlock")
    private List<ProposalTarget> proposalTargets = new ArrayList<>();

    @OneToMany(mappedBy = "block", orphanRemoval = true)
    private List<BoardBlock> boardBlocks = new ArrayList<>();
}