package com.assemblock.assemblock_be.entity;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "blocks")
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long blockId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "block_title", nullable = false)
    private String blockTitle;

    @Column(name = "block_type", nullable = false)
    private String blockType;

    @Column(name = "contribution_score", nullable = false)
    private int contributionScore;

    @Column(name = "tools_text")
    private String toolsText;

    @Column(name = "oneline_summary", nullable = false)
    private String onelineSummary;

    @Column(name = "improvement_point")
    private String improvementPoint;

    @Column(name = "result_url")
    private String resultUrl;

    @Column(name = "result_file")
    private String resultFile;
}
