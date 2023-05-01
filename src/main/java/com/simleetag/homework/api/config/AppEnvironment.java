package com.simleetag.homework.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppEnvironment {
    // OAuth
    private Client client;
    private Jwt jwt;
    private Oauth oauth;

    // Messenger
    public Messenger messenger = new Messenger();

    public static class ConnInfo {
        public String host = "localhost";
        public boolean useDummy = false;
    }

    public record Oauth(KakaoAttributes kakaoAttributes, AppleAttributes appleAttributes) {
        public record KakaoAttributes(
                String clientId,
                String redirectUri,
                String tokenUri,
                String userInformationUri
        ) {
        }

        public record AppleAttributes(
                String clientId,
                String redirectUri,
                String tokenUri,
                String teamId,
                String keyPath,
                String uri,
                String clientSecretExpiration
        ) {
        }
    }

    public record Jwt(OauthAttributes oauthAttributes, HomeAttributes homeAttributes) {
        public record OauthAttributes(long accessTokenExpiration, String secret) {
        }

        public record HomeAttributes(long accessTokenExpiration, String secret) {
        }
    }

    public record Client(KakaoOAuth kakaoOAuth, AppleOAuth appleOAuth) {
        public record KakaoOAuth(boolean useDummy) {
        }

        public record AppleOAuth(boolean useDummy) {
        }
    }

    public static class Messenger {
        public Slack slack = new Slack();

        public static class Slack extends ConnInfo {
            public String path = "test/slack/path";
        }
    }
}
