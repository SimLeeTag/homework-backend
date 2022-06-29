package com.simleetag.homework.api.utils.mockmvc;

import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public interface WebMockMvc {
    ResultActions perform(RequestBuilder requestBuilder) throws Exception;

    RequestPostProcessor userToken();

    RequestPostProcessor userToken(String accessToken);
}
