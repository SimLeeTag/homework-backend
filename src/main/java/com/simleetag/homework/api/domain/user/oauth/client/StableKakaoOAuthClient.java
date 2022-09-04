package com.simleetag.homework.api.domain.user.oauth.client;

import com.simleetag.homework.api.config.AppEnvironment;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

public record StableKakaoOAuthClient(AppEnvironment appEnvironment) implements KakaoOAuthClient {
    @Override
    public String retrieveOAuthId(String accessToken) {
        var accessTokenWithBearer = "Bearer " + accessToken;
        return WebClient.create()
                        .get()
                        .uri(appEnvironment.getOauth().kakaoAttributes().userInformationUri())
                        .header(HttpHeaders.AUTHORIZATION, accessTokenWithBearer)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
    }
}
