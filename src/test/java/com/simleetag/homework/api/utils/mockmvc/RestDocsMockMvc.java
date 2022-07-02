package com.simleetag.homework.api.utils.mockmvc;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static capital.scalable.restdocs.misc.AuthorizationSnippet.documentAuthorization;

public class RestDocsMockMvc extends AbstractWebMockMvc {

    private static final String TOKEN_REQUIRED = "User jwt required.";

    private static final String INVITATION_REQUIRED = "Invitation required";

    protected RestDocsMockMvc(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Override
    public RequestPostProcessor userToken() {
        return request -> {
            request.addHeader("Authorization", BEARER + " " + ACCESS_TOKEN);
            return documentAuthorization(request, TOKEN_REQUIRED);
        };
    }

    @Override
    public RequestPostProcessor userToken(String accessToken) {
        return request -> {
            request.addHeader("Authorization", BEARER + " " + accessToken);
            return documentAuthorization(request, TOKEN_REQUIRED);
        };
    }

    @Override
    public RequestPostProcessor invitation(String invitation) {
        return request -> {
            request.addHeader("invitation", invitation);
            return documentAuthorization(request, INVITATION_REQUIRED);
        };
    }
}
