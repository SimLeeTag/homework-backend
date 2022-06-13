package com.simleetag.homework.api.domain.oauth;

import java.util.Optional;

import com.simleetag.homework.api.domain.oauth.dto.TokenRequest;
import com.simleetag.homework.api.domain.oauth.dto.TokenResponse;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;
import com.simleetag.homework.api.domain.oauth.infra.UserRepository;
import com.simleetag.homework.api.domain.oauth.infra.provider.OAuthProvider;
import com.simleetag.homework.api.domain.oauth.infra.provider.OAuthProviderFactory;
import com.simleetag.homework.api.domain.user.User;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuthService {
    private final OAuthProviderFactory oauthProviderFactory;
    private final UserRepository userRepository;
    private final OAuthJwt oauthJwt;

    public TokenResponse signUpOrLogin(final TokenRequest tokenRequest) {
        final OAuthProvider oauthProvider = oauthProviderFactory.create(tokenRequest.getProviderType());
        final User user = new User().login(oauthProvider, tokenRequest.getAccessToken(), oauthJwt);
        final User loggedInUser = findOrSave(user);
        return new TokenResponse(loggedInUser.getAccessToken());
    }

    private User findOrSave(User user) {
        final Optional<User> savedUser = userRepository.findByOauthId(user.getOauthId());
        if (savedUser.isEmpty()) {
            return userRepository.save(user);
        }
        return savedUser.get();
    }
}
