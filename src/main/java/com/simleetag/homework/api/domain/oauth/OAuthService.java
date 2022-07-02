package com.simleetag.homework.api.domain.oauth;

import java.util.List;
import java.util.stream.Collectors;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.member.Member;
import com.simleetag.homework.api.domain.member.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final OAuthJwt oauthJwt;

    @Transactional
    public TokenResponse signUpOrLogin(final TokenRequest tokenRequest) {
        final String oauthId = oauthProviderFactory.retrieveOAuthId(tokenRequest);
        final User user = userRepository.findByOauthId(oauthId)
                                        .orElseGet(() -> new User(oauthId));

        final User savedUser = userRepository.save(user);
        final String homeworkToken = oauthJwt.createHomeworkToken(savedUser.getId());
        final List<Home> homes = memberRepository.findAllByUserId(savedUser.getId())
                                                 .stream()
                                                 .map(Member::getHome)
                                                 .collect(Collectors.toList());

        return TokenResponse.from(homeworkToken, savedUser, homes);
    }
}
