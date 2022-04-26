package com.simleetag.homework.domain.oauth;

import com.simleetag.homework.dto.AccessTokenResponse;
import com.simleetag.homework.dto.UserInformationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AppleOAuthProvider extends AbstractOAuthProvider {

    @Autowired
    private OAuthJwt oauthJwt;

    public AppleOAuthProvider() {
        this(new OAuthAttributes());
    }

    public AppleOAuthProvider(OAuthAttributes oauthAttributes) {
        super(oauthAttributes);
    }

    @Override
    public AccessTokenResponse requestAccessToken(String code) {
        return WebClient.create().post().uri(oauthAttributes.getTokenUri()).contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON).bodyValue(createRequestBodyOfToken(code)).retrieve().bodyToMono(AccessTokenResponse.class).block();
    }

    private MultiValueMap<String, String> createRequestBodyOfToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", oauthAttributes.getClientId());
        formData.add("client_secret", oauthAttributes.getClientSecret());
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", oauthAttributes.getRedirectUri());
        return formData;
    }

    @Override
    public UserInformationResponse requestUserInformation(AccessTokenResponse accessTokenResponse) {
        Long oauthId = oauthJwt.parseClaims(accessTokenResponse.getAccessToken(), "id", Long.class);
        return new UserInformationResponse(oauthId);
    }
}
