package com.simleetag.homework.api.domain.user.oauth;

import java.util.List;

import com.simleetag.homework.api.domain.home.HomeService;
import com.simleetag.homework.api.domain.home.api.dto.HomeWithMembersResponse;
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

    private final HomeService homeService;

    public TokenResponse signUpOrLogin(final TokenRequest tokenRequest) {
        final var oauthId = oauthClientFactory.getOAuthClient(tokenRequest.providerType())
                                              .retrieveOAuthId(tokenRequest.accessToken());
        final var user = userRepository.findByOauthId(oauthId)
                                       .orElseGet(() -> userRepository.save(new User(oauthId)));

        final var homeworkToken = oauthJwt.createHomeworkToken(user.getId());

        final List<HomeWithMembersResponse> homeWithMembersResponses = homeService.findAllByMemberIds(user.getMemberIds());
        return TokenResponse.from(homeworkToken, user, homeWithMembersResponses);
    }
}
