package com.assemblock.assemblock_be.Service;

import com.assemblock.assemblock_be.Dto.UserResponse;
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

    @Transactional(readOnly = true)
    public UserResponse getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + userId));

        return new UserResponse(user);
    }
}