package com.assemblock.assemblock_be.Entity;

import com.assemblock.assemblock_be.Dto.SignupDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTime implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long kakaoId;

    @Column(nullable = true)
    private String nickname;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Column(name = "portfolio_pdf_url", length = 2048)
    private String portfolioPdfUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_type")
    private UserProfileType profileType;

    @Column(name = "refresh_token")
    private String refreshToken;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private List<Role> roles = new ArrayList<>();

    @Column(name = "is_profile_complete")
    private boolean isProfileComplete = false;

    @Column(name = "review_sent_cnt")
    private Integer reviewSentCnt;

    @Column(name = "review_received_cnt")
    private Integer reviewReceivedCnt;

    @Column(name = "reliability_level", precision = 5, scale = 2)
    private BigDecimal reliabilityLevel;

    @Column(name = "is_publishing")
    private Boolean isPublishing;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Block> blocks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<com.assemblock.assemblock_be.Entity.Proposal> proposals = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<com.assemblock.assemblock_be.Entity.ProposalTarget> proposalTargets = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @Builder
    public User(Long kakaoId, String nickname) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.isProfileComplete = false;
        this.profileType = UserProfileType.TYPE_1;
        this.reviewSentCnt = 0;
        this.reviewReceivedCnt = 0;
        this.reliabilityLevel = new BigDecimal("0.0");
        this.isPublishing = true;
    }

    public void completeProfile(SignupDto dto) {
        this.nickname = dto.getNickname();
        this.roles = new ArrayList<>(dto.getRoles());
        this.profileType = dto.getUserProfileType();
        this.introduction = dto.getIntroduction();
        this.portfolioUrl = dto.getPortfolioUrl();
        this.portfolioPdfUrl = dto.getPortfolioPdfUrl();
        this.isProfileComplete = true;
    }

    public void updateProfile(String nickname, String portfolioUrl, String introduction,
                              List<Role> roles, UserProfileType profileType, String portfolioPdfUrl) {
        this.nickname = nickname;
        this.portfolioUrl = portfolioUrl;
        this.introduction = introduction;
        if (roles != null) {
            this.roles = new ArrayList<>(roles);
        }
        this.profileType = profileType;
        this.portfolioPdfUrl = portfolioPdfUrl;
    }

    public void increaseReviewSentCnt() {
        this.reviewSentCnt++;
    }

    public void increaseReviewReceivedCnt() {
        this.reviewReceivedCnt++;
    }

    // [수정 3] Refresh Token 업데이트 메서드 구현
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return Long.toString(this.id);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}