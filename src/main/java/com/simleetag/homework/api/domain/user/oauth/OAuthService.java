package com.simleetag.homework.api.domain.user.oauth;

import com.simleetag.homework.api.domain.user.User;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenRequest;
import com.simleetag.homework.api.domain.user.oauth.api.dto.TokenResponse;
import com.simleetag.homework.api.domain.user.oauth.client.OAuthClientFactory;
import com.simleetag.homework.api.domain.user.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class OAuthService {
    private final OAuthJwt oauthJwt;

    private final OAuthClientFactory oauthClientFactory;

    private final UserRepository userRepository;

    public TokenResponse signUpOrLogin(final TokenRequest tokenRequest) {
        final var oauthId = oauthClientFactory.getOAuthClient(tokenRequest.providerType())
                                              .retrieveOAuthId(tokenRequest.accessToken());
        final var user = userRepository.findByOauthId(oauthId)
                                       .orElseGet(() -> userRepository.save(new User(oauthId)));

        final var homeworkToken = oauthJwt.createHomeworkToken(user.getId());

        return TokenResponse.from(homeworkToken, user);
    }
}
