package com.simleetag.homework.domain.oauth;

abstract class AbstractOAuthProvider implements OAuthProvider {
    protected OAuthAttributes oauthAttributes;

    protected AbstractOAuthProvider() {
        this(new OAuthAttributes());
    }

    protected AbstractOAuthProvider(OAuthAttributes oauthAttributes) {
        this.oauthAttributes = oauthAttributes;
    }
}
