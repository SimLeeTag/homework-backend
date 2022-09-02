package com.simleetag.homework.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppEnvironment {
    private Client client;
    private Jwt jwt;
    private Oauth oauth;

    public record Oauth(KakaoAttributes kakaoAttributes, AppleAttributes appleAttributes) {
        public record KakaoAttributes(
                String clientId,
                String redirectUri,
                String tokenUri,
                String userInformationUri
        ) {}

        public record AppleAttributes(
                String clientId,
                String redirectUri,
                String tokenUri,
                String teamId,
                String keyPath,
                String uri,
                String clientSecretExpiration
        ) {}
    }

    public record Jwt(OauthAttributes oauthAttributes, HomeAttributes homeAttributes) {
        public record OauthAttributes(long accessTokenExpiration, String secret) {}

        public record HomeAttributes(long accessTokenExpiration, String secret) {}
    }

    public record Client(KakaoOAuth kakaoOAuth, AppleOAuth appleOAuth) {
        public record KakaoOAuth(boolean useDummy) {}

        public record AppleOAuth(boolean useDummy) {}
    }
}
