package com.simleetag.homework.domain.oauth;

public enum ProviderType {
    KAKAO, APPLE;

    public static ProviderType from(String providerType) {
        return ProviderType.valueOf(providerType.toUpperCase());
    }
}
