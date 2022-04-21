package com.simleetag.homework.service;

import java.util.Optional;

import com.simleetag.homework.domain.User;
import com.simleetag.homework.domain.oauth.OAuthJwt;
import com.simleetag.homework.domain.oauth.OAuthProvider;
import com.simleetag.homework.domain.oauth.OAuthProviderFactory;
import com.simleetag.homework.dto.AccessTokenRequest;
import com.simleetag.homework.repository.UserRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuthService {
    private final OAuthProviderFactory oauthProviderFactory;
    private final UserRepository userRepository;
    private final OAuthJwt oauthJwt;

    public String signUpOrLogin(final AccessTokenRequest accessTokenRequest) {
        final OAuthProvider oauthProvider = oauthProviderFactory.create(accessTokenRequest.getProviderType());
        final User user = new User().login(oauthProvider, accessTokenRequest.getCode());
        final User savedUser = findOrSave(user);
        return oauthJwt.createWithUserId(savedUser.getId());
    }

    private User findOrSave(User user) {
        final Optional<User> savedUser = userRepository.findByOauthId(user.getOauthId());
        if (savedUser.isEmpty()) {
            return userRepository.save(user);
        }
        return savedUser.get();
    }
}
