package com.assemblock.assemblock_be.Entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserTechPartId implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long techpartId;

    public UserTechPartId() {}
    public UserTechPartId(Long userId, Long techpartId) {
        this.userId = userId;
        this.techpartId = techpartId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTechPartId that = (UserTechPartId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(techpartId, that.techpartId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, techpartId);
    }
}