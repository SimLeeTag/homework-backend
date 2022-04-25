package com.simleetag.homework.domain.oauth;

import java.util.Arrays;

import com.simleetag.homework.exception.OAuthException;

public enum ProviderType {
    KAKAO, APPLE;

    public static ProviderType from(String providerType) {
        return Arrays.stream(ProviderType.values())
                     .filter(provider -> provider.name().equals(providerType.toUpperCase()))
                     .findAny()
                     .orElseThrow(() -> new OAuthException(providerType + "의 OAuth 기능은 제공하지 않습니다."));
    }
}
