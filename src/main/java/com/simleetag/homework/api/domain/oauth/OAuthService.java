package com.simleetag.homework.api.domain.oauth;

import java.util.Optional;

import com.simleetag.homework.api.domain.oauth.dto.TokenRequest;
import com.simleetag.homework.api.domain.oauth.dto.TokenResponse;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;
import com.simleetag.homework.api.domain.oauth.infra.provider.OAuthProviderFactory;
import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuthService {
    private final OAuthProviderFactory oauthProviderFactory;
    private final UserRepository userRepository;
    private final OAuthJwt oauthJwt;

    @Transactional
    public TokenResponse signUpOrLogin(final TokenRequest tokenRequest) {
        final String oauthId = oauthProviderFactory.retrieveOAuthId(tokenRequest);
        final Optional<User> savedUser = userRepository.findByOauthId(oauthId);
        if (savedUser.isPresent()) {
            return TokenResponse.from(savedUser.get());
        }

        final User loggedInUser = userRepository.save(new User())
                                                .login(oauthId, oauthJwt);
        return TokenResponse.from(loggedInUser);
    }
}
