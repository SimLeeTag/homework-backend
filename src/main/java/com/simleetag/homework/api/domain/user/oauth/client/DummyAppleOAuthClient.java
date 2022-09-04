package com.simleetag.homework.api.domain.user.oauth.client;

import java.util.Random;

public class DummyAppleOAuthClient implements AppleOAuthClient {
    @Override
    public String retrieveOAuthId(String accessToken) {
        return new Random().nextLong() + "";
    }
}
