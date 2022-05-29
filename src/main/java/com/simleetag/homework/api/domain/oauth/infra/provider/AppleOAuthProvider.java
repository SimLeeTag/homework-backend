package com.simleetag.homework.api.domain.oauth.infra.provider;

import com.simleetag.homework.api.domain.oauth.OAuthAttributes;
import com.simleetag.homework.api.domain.oauth.dto.UserInformationResponse;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public UserInformationResponse requestUserInformation(String accessToken) {
        Long oauthId = oauthJwt.parseClaims(accessToken, "id", Long.class);
        return new UserInformationResponse(oauthId);
    }
}
