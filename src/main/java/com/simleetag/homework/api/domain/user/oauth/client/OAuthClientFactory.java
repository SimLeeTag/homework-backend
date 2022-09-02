package com.simleetag.homework.api.domain.user.oauth.client;

import com.simleetag.homework.api.domain.user.oauth.ProviderType;

public record OAuthClientFactory(
        AppleOAuthClient appleOAuthClient,
        KakaoOAuthClient kakaoOAuthClient
) {
    public OAuthClient getOAuthClient(ProviderType providerType) {
        return switch (providerType) {
            case KAKAO -> kakaoOAuthClient;
            case APPLE -> appleOAuthClient;
        };
    }
}
