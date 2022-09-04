package com.simleetag.homework.api.domain.user.oauth.client;

public interface OAuthClient {
    String retrieveOAuthId(String accessToken);
}
