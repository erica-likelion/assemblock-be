package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "User_techparts")
public class UserTechPart {
    @EmbeddedId
    private UserTechPartId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("techpartId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "techpart_id")
    private TechPart techPart;

    @Builder
    public UserTechPart(User user, TechPart techPart) {
        this.user = user;
        this.techPart = techPart;
        this.id = new UserTechPartId(user.getId(), techPart.getId());
    }
}