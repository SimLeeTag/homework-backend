package com.simleetag.homework.api.domain.oauth.infra.provider;

import java.util.Arrays;

import com.simleetag.homework.api.common.exception.OAuthException;

public enum ProviderType {
    KAKAO, APPLE;

    public static ProviderType from(String providerType) {
        return Arrays.stream(ProviderType.values())
                     .filter(provider -> provider.name().equals(providerType.toUpperCase()))
                     .findAny()
                     .orElseThrow(() -> new OAuthException(providerType + "의 OAuth 기능은 제공하지 않습니다."));
    }
}
