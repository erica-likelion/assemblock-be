package com.assemblock.assemblock_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "User") // 쿼리문 테이블명 그대로
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nickname", nullable = false, length = 255)
    private String nickname;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    // MySQL SET 타입 → 일단 String 으로 매핑 (Plan,Design,PM,FrontEnd,BackEnd 조합)
    @Column(name = "main_role", nullable = false, length = 255)
    private String mainRole;

    @Column(name = "introduction", length = 255)
    private String introduction;

    @Column(name = "profile_type", nullable = false, length = 20)
    private String profileType = "Type_1";

    @Column(name = "portfolio_url", length = 255)
    private String portfolioUrl;

    @Column(name = "portfolio_pdf_url", length = 2048)
    private String portfolioPdfUrl;

    @Column(name = "review_sent_cnt", nullable = false)
    private int reviewSentCnt = 0;

    @Column(name = "review_received_cnt", nullable = false)
    private int reviewReceivedCnt = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "is_publishing", nullable = false)
    private boolean isPublishing = true;

    // ⭐ 서비스에서 new User(id) 쓸 수 있게 하는 생성자
    public User(Long userId) {
        this.userId = userId;
    }
}
