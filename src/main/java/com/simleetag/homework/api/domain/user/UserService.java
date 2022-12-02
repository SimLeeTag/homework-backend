package com.simleetag.homework.api.domain.user;

import java.util.List;

import com.simleetag.homework.api.domain.user.api.UserMaintenanceController;
import com.simleetag.homework.api.domain.user.api.UserSignUpRequest;
import com.simleetag.homework.api.domain.user.api.dto.UserProfileRequest;
import com.simleetag.homework.api.domain.user.repository.UserDslRepository;
import com.simleetag.homework.api.domain.user.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private static final String NOT_FOUND_MESSAGE = "UserID[%d]에 해당하는 유저가 존재하지 않습니다.";

    private final UserRepository userRepository;

    private final UserDslRepository userDslRepository;

    public User findUserWithHomeAndMembers(Long userId) {
        return userDslRepository.findUserWithHomeAndMembers(userId)
                                .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND_MESSAGE, userId)));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new IllegalArgumentException(String.format(NOT_FOUND_MESSAGE, userId)));
    }

    public User add(UserSignUpRequest request) {
        User user = new User(request.oauthId(), request.profileImage(), request.userName());
        return userRepository.save(user);
    }

    public User editProfile(Long userId, UserProfileRequest request) {
        final var user = findById(userId);
        user.editProfile(request);
        return user;
    }

    public User expire(Long userId) {
        final var user = findById(userId);
        user.expire();
        return user;
    }

    public List<User> findAll(UserMaintenanceController.UserSearchCondition condition) {
        return userRepository.findAll(condition.toSpecs());
    }
}
