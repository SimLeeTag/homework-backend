package com.simleetag.homework.api.domain.oauth.infra.provider;

import com.simleetag.homework.api.domain.oauth.OAuthAttributes;
import com.simleetag.homework.api.domain.oauth.dto.AccessTokenResponse;
import com.simleetag.homework.api.domain.oauth.dto.UserInformationResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

public class KakaoOAuthProvider extends AbstractOAuthProvider {

    protected KakaoOAuthProvider(OAuthAttributes oauthAttributes) {
        super(oauthAttributes);
    }

    @Override
    public AccessTokenResponse requestAccessToken(String code) {
        return WebClient.create()
                        .post()
                        .uri(oauthAttributes.getTokenUri())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(createRequestBodyOfToken(code))
                        .retrieve()
                        .bodyToMono(AccessTokenResponse.class)
                        .block();
    }

    private MultiValueMap<String, String> createRequestBodyOfToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", oauthAttributes.getClientId());
        formData.add("redirect_uri", oauthAttributes.getRedirectUri());
        formData.add("code", code);
        return formData;
    }

    @Override
    public UserInformationResponse requestUserInformation(AccessTokenResponse accessTokenResponse) {
        return WebClient.create()
                        .get()
                        .uri(oauthAttributes.getUserInformationUri())
                        .header(HttpHeaders.AUTHORIZATION, accessTokenResponse.getAccessTokenAsBearer())
                        .retrieve()
                        .bodyToMono(UserInformationResponse.class)
                        .block();
    }
}
