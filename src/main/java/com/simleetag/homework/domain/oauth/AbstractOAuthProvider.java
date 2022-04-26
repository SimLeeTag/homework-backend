package com.simleetag.homework.domain.oauth;

abstract class AbstractOAuthProvider implements OAuthProvider {
    protected OAuthAttributes oauthAttributes;

    public AbstractOAuthProvider() {
        this(new OAuthAttributes());
    }

    protected AbstractOAuthProvider(OAuthAttributes oauthAttributes) {
        this.oauthAttributes = oauthAttributes;
    }
}
