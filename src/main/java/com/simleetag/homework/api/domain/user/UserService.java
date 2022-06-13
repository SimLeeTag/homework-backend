package com.simleetag.homework.api.domain.user;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findByAccessToken(String accessToken) {
        return userRepository.findByAccessToken(accessToken)
                             .orElseThrow(() -> new IllegalArgumentException("액세스 토큰을 가진 유저가 존재하지 않습니다."));
    }
}
