package com.simleetag.homework.domain.oauth;

import com.simleetag.homework.dto.AccessTokenResponse;
import com.simleetag.homework.dto.UserInformationResponse;

import java.io.IOException;

public interface OAuthProvider {
    AccessTokenResponse requestAccessToken(String code) throws IOException;

    UserInformationResponse requestUserInformation(AccessTokenResponse accessTokenResponse);
}
