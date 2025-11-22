// 로그인 구현 후 확인 필요

package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}