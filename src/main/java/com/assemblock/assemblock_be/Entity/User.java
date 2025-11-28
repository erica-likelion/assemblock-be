package com.assemblock.assemblock_be.Entity;

import com.assemblock.assemblock_be.Dto.SignupDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "User")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long kakaoId;

    @Column(nullable = false)
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

    @Builder
    public User(Long kakaoId) {
        this.kakaoId = kakaoId;
        this.isProfileComplete = false;
        this.reviewSentCnt = 0;
        this.reviewReceivedCnt = 0;
        this.reliabilityLevel = new BigDecimal("0.0");
        this.isPublishing = true;
    }

    /**
     * 2단계 회원가입 (프로필 완성)
     */
    public void completeProfile(SignupDto dto) {
        this.nickname = dto.getNickname();
        this.roles = new ArrayList<>(dto.getRoles());

        this.profileType = UserProfileType.values()[dto.getProfileImageIndex() - 1];

        this.introduction = dto.getIntroduction();
        this.portfolioUrl = dto.getPortfolioUrl();
        this.portfolioPdfUrl = dto.getPortfolioPdfUrl();

        this.isProfileComplete = true;
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