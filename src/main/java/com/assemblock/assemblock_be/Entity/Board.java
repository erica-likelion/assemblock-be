package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "Board",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_board_name",
                        columnNames = {"user_id", "board_name"}
                )
        }
)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "board_name", nullable = false)
    private String boardName;

    @Column(name = "board_memo", columnDefinition = "TEXT")
    private String boardMemo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<BoardBlock> boardBlocks = new ArrayList<>();

    public void update(String boardName, String boardMemo) {
        if (boardName != null && !boardName.isBlank()) {
            this.boardName = boardName;
        }
        if (boardMemo != null) {
            this.boardMemo = boardMemo;
        }
    }
}

