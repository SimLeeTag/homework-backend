package com.simleetag.homework.api.domain.oauth.infra.provider;

import com.simleetag.homework.api.domain.oauth.OAuthAttributes;
import com.simleetag.homework.api.domain.oauth.dto.UserInformationResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

public class KakaoOAuthProvider extends AbstractOAuthProvider {

    protected KakaoOAuthProvider(OAuthAttributes oauthAttributes) {
        super(oauthAttributes);
    }

    @Override
    public UserInformationResponse requestUserInformation(String accessToken) {
        String accessTokenWithBearer = "bearer " + accessToken;
        return WebClient.create()
                        .get()
                        .uri(oauthAttributes.getUserInformationUri())
                        .header(HttpHeaders.AUTHORIZATION, accessTokenWithBearer)
                        .retrieve()
                        .bodyToMono(UserInformationResponse.class)
                        .block();
    }
}
