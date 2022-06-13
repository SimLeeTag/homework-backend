package com.simleetag.homework.api.domain.oauth.infra.provider;

import com.simleetag.homework.api.domain.oauth.OAuthAttributes;
import com.simleetag.homework.api.domain.oauth.dto.UserInformationResponse;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;

public class AppleOAuthProvider extends AbstractOAuthProvider {

    private final OAuthJwt oauthJwt;

    public AppleOAuthProvider(OAuthAttributes oauthAttributes, OAuthJwt oauthJwt) {
        super(oauthAttributes);
        this.oauthJwt = oauthJwt;
    }

    @Override
    public UserInformationResponse requestUserInformation(String accessToken) {
        String oauthId = oauthJwt.parseClaims(accessToken, "sub", String.class);
        return new UserInformationResponse(oauthId);
    }
}
