package com.simleetag.homework.api.domain.oauth.infra.provider;

import com.simleetag.homework.api.domain.oauth.dto.UserInformationResponse;

public interface OAuthProvider {
    UserInformationResponse requestUserInformation(String accessToken);
}
