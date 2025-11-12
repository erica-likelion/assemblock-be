package com.assemblock.assemblock_be.Entity;

import com.assemblock.assemblock_be.Dto.SignupRequestDto;
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
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long kakaoId;

    @Column(nullable = true)
    private String nickname;

    // email 필드 완전 삭제

    @Column(name = "introduction")
    private String introduction;
    // ... (기존 portfolioUrl, portfolioPdfUrl, profileImageIndex 필드) ...
    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Column(name = "portfolio_pdf_url", length = 2048)
    private String portfolioPdfUrl;

    @Column(name = "profile_image_index")
    private Integer profileImageIndex;

    @ElementCollection(fetch = FetchType.EAGER)
// ... (기존 roles 필드) ...
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<Role> roles = new ArrayList<>();

    @Column(name = "is_profile_complete")
// ... (기존 isProfileComplete 필드) ...
    private boolean isProfileComplete = false;

    // --- ▽ ERD 필드 추가 ▽ ---
    @Column(name = "user_level")
// ... (기존 userLevel, reliabilityCnt, reliabilityLevel, isPublishing 필드) ...
    private Integer userLevel;

    @Column(name = "reliability_cnt")
    private Integer reliabilityCnt;

    @Column(name = "reliability_level", precision = 5, scale = 2)
    private BigDecimal reliabilityLevel;

    @Column(name = "is_publishing")
    private Boolean isPublishing;

    @Builder
    public User(Long kakaoId) {
// ... (기존 Builder) ...
        this.kakaoId = kakaoId;
        this.isProfileComplete = false;
        // 기본값 초기화
        this.userLevel = 1;
        this.reliabilityCnt = 0;
        this.reliabilityLevel = new BigDecimal("0.0");
        this.isPublishing = true; // 기본 공개
    }

    // 2단계 정보 입력을 위한 업데이트 메서드 (수정됨)
    public void completeProfile(SignupRequestDto dto) {
        this.nickname = dto.getNickname();
        this.roles = new ArrayList<>(dto.getRoles());
        this.profileImageIndex = dto.getProfileImageIndex();
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