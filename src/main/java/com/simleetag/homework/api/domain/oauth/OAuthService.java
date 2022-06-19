package com.simleetag.homework.api.domain.oauth;

import java.util.List;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeService;
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
    private final HomeService homeService;
    private final UserRepository userRepository;
    private final OAuthJwt oauthJwt;

    @Transactional
    public TokenResponse signUpOrLogin(final TokenRequest tokenRequest) {
        final String oauthId = oauthProviderFactory.retrieveOAuthId(tokenRequest);
        final User loginUser = userRepository.findByOauthId(oauthId)
                                             .orElseGet(() -> userRepository.save(new User()))
                                             .login(oauthId, oauthJwt);

        final List<Home> homes = homeService.findAllWithMembers(loginUser.getId());
        return TokenResponse.from(loginUser, homes);
    }
}
