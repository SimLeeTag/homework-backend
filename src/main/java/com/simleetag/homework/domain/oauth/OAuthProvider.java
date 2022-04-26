package com.simleetag.homework.domain.oauth;

import com.simleetag.homework.dto.AccessTokenResponse;
import com.simleetag.homework.dto.UserInformationResponse;

public interface OAuthProvider {
    AccessTokenResponse requestAccessToken(String code);

    UserInformationResponse requestUserInformation(AccessTokenResponse accessTokenResponse);
}
