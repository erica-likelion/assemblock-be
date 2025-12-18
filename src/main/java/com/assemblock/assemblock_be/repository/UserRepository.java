package com.assemblock.assemblock_be.repository;

import com.assemblock.assemblock_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
