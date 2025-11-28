package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.UserResponseDto;
import com.assemblock.assemblock_be.Entity.User;
import com.assemblock.assemblock_be.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 내 정보(프로필) 조회
     * @param userId (JWT 토큰에서 가져온 사용자 ID)
     * @return UserResponseDto
     */
    @Transactional(readOnly = true)
    public UserResponseDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + userId));

        // Entity를 DTO로 변환하여 반환
        return new UserResponseDto(user);
    }
}