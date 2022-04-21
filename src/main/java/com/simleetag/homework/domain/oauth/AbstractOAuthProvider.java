package com.simleetag.homework.domain.oauth;

abstract class AbstractOAuthProvider implements OAuthProvider {
    protected OAuthAttributes oauthAttributes;

    protected AbstractOAuthProvider(OAuthAttributes oauthAttributes) {
        this.oauthAttributes = oauthAttributes;
    }
}
