package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Convert(converter = MemberRoleSetConverter.class)
    @Column(name = "main_role", nullable = false)
    private Set<MemberRole> mainRoles = new HashSet<>();

    @Column(name = "introduction")
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_type", nullable = false)
    private ProfileType profileType = ProfileType.Type_1;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Column(name = "portfolio_pdf_url", length = 2048)
    private String portfolioPdfUrl;

    @Column(name = "review_sent_cnt", nullable = false)
    private Integer reviewSentCnt = 0;

    @Column(name = "review_received_cnt", nullable = false)
    private Integer reviewReceivedCnt = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_publishing", nullable = false)
    private Boolean isPublishing = true;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Block> blocks = new ArrayList<>();

    @OneToMany(mappedBy = "proposer")
    private List<Proposal> proposals = new ArrayList<>();

    @OneToMany(mappedBy = "proposer")
    private List<ProposalTarget> proposalTargets = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    public void updateProfile(String newNickname, String newPortfolioUrl, String newIntroduction,
                              Set<MemberRole> newMainRoles, ProfileType newProfileType, String newPortfolioPdfUrl) {
        if (newNickname != null && !newNickname.isBlank()) {
            this.nickname = newNickname;
        }
        if (newPortfolioUrl != null) {
            this.portfolioUrl = newPortfolioUrl;
        }
        if (newIntroduction != null) {
            this.introduction = newIntroduction;
        }
        if (newMainRoles != null && !newMainRoles.isEmpty()) {
            this.mainRoles = newMainRoles;
        }
        if (newProfileType != null) {
            this.profileType = newProfileType;
        }
        if (newPortfolioPdfUrl != null) {
            this.portfolioPdfUrl = newPortfolioPdfUrl;
        }
    }

    @Converter
    public static class MemberRoleSetConverter implements AttributeConverter<Set<MemberRole>, String> {
        @Override
        public String convertToDatabaseColumn(Set<MemberRole> attribute) {
            if (attribute == null || attribute.isEmpty()) {
                return "";
            }
            return attribute.stream()
                    .map(MemberRole::name)
                    .collect(Collectors.joining(","));
        }

        @Override
        public Set<MemberRole> convertToEntityAttribute(String dbData) {
            if (dbData == null || dbData.isBlank()) {
                return new HashSet<>();
            }
            return Arrays.stream(dbData.split(","))
                    .map(MemberRole::valueOf)
                    .collect(Collectors.toSet());
        }
    }

    public void increaseReviewSentCnt() {
        this.reviewSentCnt++;
    }

    public void increaseReviewReceivedCnt() {
        this.reviewReceivedCnt++;
    }
}