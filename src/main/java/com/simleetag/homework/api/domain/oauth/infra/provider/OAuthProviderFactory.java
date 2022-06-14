package com.simleetag.homework.api.domain.oauth.infra.provider;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

import com.simleetag.homework.api.domain.oauth.OAuthAttributes;
import com.simleetag.homework.api.domain.oauth.dto.TokenRequest;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.RequiredArgsConstructor;

@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth")
public class OAuthProviderFactory {

    private final Map<String, OAuthAttributes> attribute;
    private final Map<ProviderType, OAuthProvider> providers = new HashMap<>();

    @Autowired
    private OAuthJwt oauthJwt;

    @PostConstruct
    public void initProvider() {
        for (Map.Entry<String, OAuthAttributes> entry : attribute.entrySet()) {
            final String ProviderTypeName = entry.getKey();
            final ProviderType providerType = ProviderType.from(ProviderTypeName);
            final OAuthAttributes oauthAttributes = attribute.getOrDefault(ProviderTypeName, new OAuthAttributes());

            if (providerType == ProviderType.KAKAO) {
                providers.put(providerType, new KakaoOAuthProvider(oauthAttributes));
            } else if (providerType == ProviderType.APPLE) {
                providers.put(providerType, new AppleOAuthProvider(oauthAttributes, oauthJwt));
            }
        }
    }

    public String retrieveOAuthId(TokenRequest tokenRequest) {
        return providers.get(tokenRequest.getProviderType())
                        .requestUserInformation(tokenRequest.getAccessToken())
                        .getId();
    }
}
