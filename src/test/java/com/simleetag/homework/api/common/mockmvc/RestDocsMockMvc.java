package com.simleetag.homework.api.common.mockmvc;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import lombok.RequiredArgsConstructor;

import static capital.scalable.restdocs.misc.AuthorizationSnippet.documentAuthorization;

@RequiredArgsConstructor
public final class RestDocsMockMvc {
    // 헤더 샘플
    private static final String ACCESS_TOKEN = "sample.homework.token";
    private static final String BEARER = "Bearer";
    // 헤더 검증
    private static final String INVITATION_REQUIRED = "Invitation required";
    private static final String TOKEN_REQUIRED = "User jwt required.";
    private final MockMvc mockMvc;
    private final boolean document;

    public ResultActions perform(RequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder);
    }

    public RequestPostProcessor userToken() {
        return request -> {
            request.addHeader("Authorization", BEARER + " " + ACCESS_TOKEN);
            return document ? documentAuthorization(request, TOKEN_REQUIRED) : request;
        };
    }

    public RequestPostProcessor userToken(String accessToken) {
        return request -> {
            request.addHeader("Authorization", BEARER + " " + accessToken);
            return document ? documentAuthorization(request, TOKEN_REQUIRED) : request;
        };
    }

    public RequestPostProcessor invitation(String invitation) {
        return request -> {
            request.addHeader("invitation", invitation);
            return document ? documentAuthorization(request, INVITATION_REQUIRED) : request;
        };
    }
}
