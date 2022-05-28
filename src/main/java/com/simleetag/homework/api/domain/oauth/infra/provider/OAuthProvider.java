package com.simleetag.homework.api.domain.oauth.infra.provider;

import java.io.IOException;

import com.simleetag.homework.api.domain.oauth.dto.AccessTokenResponse;
import com.simleetag.homework.api.domain.oauth.dto.UserInformationResponse;

public interface OAuthProvider {
    AccessTokenResponse requestAccessToken(String code) throws IOException;

    UserInformationResponse requestUserInformation(AccessTokenResponse accessTokenResponse);
}
