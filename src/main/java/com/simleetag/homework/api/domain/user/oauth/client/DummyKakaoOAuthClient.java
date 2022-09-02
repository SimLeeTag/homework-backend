package com.simleetag.homework.api.domain.user.oauth.client;

import java.util.Random;

public class DummyKakaoOAuthClient implements KakaoOAuthClient {
    @Override
    public String retrieveOAuthId(String accessToken) {
        return new Random().nextLong() + "";
    }
}
