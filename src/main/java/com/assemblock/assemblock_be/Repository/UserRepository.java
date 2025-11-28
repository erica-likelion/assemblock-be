package com.assemblock.assemblock_be.Repository;

import com.assemblock.assemblock_be.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 카카오 ID로 사용자를 찾는 메서드
    Optional<User> findByKakaoId(Long kakaoId);
}