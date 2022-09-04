package com.simleetag.homework.api.domain.user.oauth.client;

import com.simleetag.homework.api.config.AppEnvironment;
import com.simleetag.homework.api.domain.user.oauth.OAuthJwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class OAuthClientConfiguration {
    private final AppEnvironment appEnvironment;
    private final OAuthJwt oauthJwt;

    @Bean
    public AppleOAuthClient appleOAuthClient() {
        var env = appEnvironment.getClient().appleOAuth();

        if (env.useDummy())
            return new DummyAppleOAuthClient();
        else
            return new StableAppleOAuthClient(oauthJwt);
    }

    @Bean
    public KakaoOAuthClient kakaoOAuthClient() {
        var env = appEnvironment.getClient().appleOAuth();

        if (env.useDummy())
            return new DummyKakaoOAuthClient();
        else
            return new StableKakaoOAuthClient(appEnvironment);
    }

    @Bean
    public OAuthClientFactory oauthClientFactory() {
        return new OAuthClientFactory(appleOAuthClient(), kakaoOAuthClient());
    }
}
