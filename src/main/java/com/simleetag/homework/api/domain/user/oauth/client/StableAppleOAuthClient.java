package com.simleetag.homework.api.domain.user.oauth.client;

import com.simleetag.homework.api.domain.user.oauth.OAuthJwt;

public record StableAppleOAuthClient(OAuthJwt oauthJwt) implements AppleOAuthClient {
    @Override
    public String retrieveOAuthId(String accessToken) {
        return oauthJwt.parseClaims(accessToken, "sub", String.class);
    }
}
