package com.simleetag.homework.api.domain.user.oauth;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.simleetag.homework.api.common.exception.OAuthException;

public enum ProviderType {
    KAKAO, APPLE;

    private static final Map<String, ProviderType> PROVIDER_TYPE_MAP = new HashMap<>();

    static {
        for (ProviderType type : ProviderType.values()) {
            PROVIDER_TYPE_MAP.put(type.name(), type);
        }
    }

    public static ProviderType from(String providerType) {
        final ProviderType type = PROVIDER_TYPE_MAP.get(providerType.toUpperCase());
        if (Objects.isNull(type)) {
            throw new OAuthException(providerType + "의 OAuth 기능은 제공하지 않습니다.");
        }

        return type;
    }
}
