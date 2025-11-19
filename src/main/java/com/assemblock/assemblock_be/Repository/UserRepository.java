package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// 로그인 구현 코드와 함께 점검 필요
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}