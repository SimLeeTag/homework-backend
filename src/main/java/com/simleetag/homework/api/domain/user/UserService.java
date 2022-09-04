package com.simleetag.homework.api.domain.user;

import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new IllegalArgumentException(String.format("UserID[%d]에 해당하는 유저가 존재하지 않습니다.", userId)));
    }

    public User findUserWithMembersByUserId(Long userId) {
        return userRepository.findUserWithMembersById(userId)
                             .orElseThrow(() -> new IllegalArgumentException(String.format("UserID[%d]에 해당하는 유저가 존재하지 않습니다.", userId)));
    }


    public User editProfile(Long userId, UserProfileRequest request) {
        final var user = findById(userId);
        user.editProfile(request);
        return user;
    }
}
