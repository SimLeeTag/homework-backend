package com.simleetag.homework.api.utils.mockmvc;

import java.util.Objects;

import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class HttpMockMvc extends AbstractWebMockMvc {

    private static HttpMockMvc httpMockMvc;

    protected HttpMockMvc(MockMvc mockMvc) {
        super(mockMvc);
    }

    public static HttpMockMvc of(ApplicationContext applicationContext) {
        if (Objects.isNull(httpMockMvc)) {
            httpMockMvc = new HttpMockMvc(standaloneMockMvcBuilder(applicationContext).build());
        }

        return httpMockMvc;
    }

    @Override
    public RequestPostProcessor userToken() {
        return request -> {
            request.addHeader("Authorization", BEARER + " " + ACCESS_TOKEN);
            return request;
        };
    }

    @Override
    public RequestPostProcessor userToken(String accessToken) {
        return request -> {
            request.addHeader("Authorization", BEARER + " " + accessToken);
            return request;
        };
    }

    @Override
    public RequestPostProcessor invitation(String invitation) {
        return request -> {
            request.addHeader("invitation", invitation);
            return request;
        };
    }
}
