package com.simleetag.homework.api.domain.oauth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthAttributes {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUri;
    private String userInformationUri;
    private String teamId;
    private String keyPath;
    private String uri;
    private String clientSecretExpiration;
}
