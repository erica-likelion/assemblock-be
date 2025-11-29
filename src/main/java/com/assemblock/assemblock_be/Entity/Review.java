package com.assemblock.assemblock_be.Entity;

import com.assemblock.assemblock_be.Project;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "Review",
        indexes = {
                @Index(name = "idx_reviewed_id", columnList = "reviewed_id"),
                @Index(name = "idx_project_id", columnList = "project_id")
        }
)
@DynamicUpdate
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_Reviews_user_id"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_Reviews_reviewed_id"))
    private User reviewedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_Reviews_project_id"))
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "review", nullable = false)
    private com.assemblock.assemblock_be.Entity.ReviewRating review;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
