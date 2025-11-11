package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "profile_url")
    private String profileImageUrl;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Column(name = "user_level", nullable = false)
    private Integer userLevel;

    @Column(name = "reliability_cnt", nullable = false)
    private Integer reliabilityCnt;

    @Column(name = "reliability_level", nullable = false, precision = 5, scale = 2)
    private BigDecimal reliabilityLevel;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_publishing", nullable = false)
    private Boolean isPublishing = true;

    @Column(name = "introduction")
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "main_role", nullable = false)
    private MemberRole mainRole;

    @Column(name = "portfolio_pdf_url", length = 2048)
    private String portfolioPdfUrl;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Block> blocks = new ArrayList<>();

    @OneToMany(mappedBy = "proposer")
    private List<Proposal> proposals = new ArrayList<>();

    @OneToMany(mappedBy = "proposer")
    private List<ProposalTarget> proposalTargets = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    public void updateProfile(String newNickname, String newPortfolioUrl, String newIntroduction, MemberRole newMainRole, String newProfileImageUrl, String newPortfolioPdfUrl) {
        if (newNickname != null && !newNickname.isBlank()) {
            this.nickname = newNickname;
        }
        if (newPortfolioUrl != null) {
            this.portfolioUrl = newPortfolioUrl;
        }
        if (newIntroduction != null) {
            this.introduction = newIntroduction;
        }
        if (newMainRole != null) {
            this.mainRole = newMainRole;
        }
        if (newProfileImageUrl != null) {
            this.profileImageUrl = newProfileImageUrl;
        }
        if (newPortfolioPdfUrl != null) {
            this.portfolioPdfUrl = newPortfolioPdfUrl;
        }
    }
}