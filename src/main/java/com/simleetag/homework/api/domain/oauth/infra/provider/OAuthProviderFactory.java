package com.simleetag.homework.api.domain.oauth.infra.provider;

import java.util.Map;

import com.simleetag.homework.api.domain.oauth.OAuthAttributes;
import com.simleetag.homework.api.common.exception.OAuthException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.RequiredArgsConstructor;

@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth")
public class OAuthProviderFactory {

    private final Map<String, OAuthAttributes> attribute;

    public OAuthProvider create(ProviderType providerType) {
        final OAuthAttributes oauthAttributes = attribute.getOrDefault(providerType.name(), new OAuthAttributes());
        if (providerType == ProviderType.KAKAO) {
            return new KakaoOAuthProvider(oauthAttributes);
        } else if (providerType == ProviderType.APPLE) {
            return new AppleOAuthProvider(oauthAttributes);
        }

        throw new OAuthException(providerType.name() + "의 OAuth 기능은 제공하지 않습니다.");
    }
}
