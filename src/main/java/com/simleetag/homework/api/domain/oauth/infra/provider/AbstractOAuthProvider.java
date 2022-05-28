package com.simleetag.homework.api.domain.oauth.infra.provider;

import com.simleetag.homework.api.domain.oauth.OAuthAttributes;

abstract class AbstractOAuthProvider implements OAuthProvider {
    protected OAuthAttributes oauthAttributes;

    protected AbstractOAuthProvider() {
        this(new OAuthAttributes());
    }

    protected AbstractOAuthProvider(OAuthAttributes oauthAttributes) {
        this.oauthAttributes = oauthAttributes;
    }
}
