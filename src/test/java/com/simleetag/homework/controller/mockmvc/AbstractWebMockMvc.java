package com.simleetag.homework.controller.mockmvc;

import java.nio.charset.StandardCharsets;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.filter.CharacterEncodingFilter;

public abstract class AbstractWebMockMvc implements WebMockMvc {

    protected static final String BEARER = "Bearer";
    protected static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    protected final MockMvc mockMvc;

    protected AbstractWebMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Override
    public final ResultActions perform(RequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder);
    }

    public static StandaloneMockMvcBuilder defaultMockMvcBuilder(ApplicationContext applicationContext) {
        Object[] controllers = findControllers(applicationContext);

        return MockMvcBuilders.standaloneSetup(controllers)
                              .addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true));
    }

    private static Object[] findControllers(ApplicationContext applicationContext) {
        return applicationContext.getBeansWithAnnotation(Controller.class)
                                 .values()
                                 .toArray();
    }
}
