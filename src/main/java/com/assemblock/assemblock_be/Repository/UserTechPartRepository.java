package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Entity.UserTechPart;
import com.assemblock.assemblock_be.Entity.UserTechPartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTechPartRepository extends JpaRepository<UserTechPart, UserTechPartId> {
    void deleteAllByUser(User user);
}