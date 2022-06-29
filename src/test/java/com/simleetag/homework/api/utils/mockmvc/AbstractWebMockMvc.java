package com.simleetag.homework.api.utils.mockmvc;

import java.nio.charset.StandardCharsets;

import com.simleetag.homework.api.common.exception.GlobalCustomException;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

public abstract class AbstractWebMockMvc implements WebMockMvc {

    protected static final String BEARER = "Bearer";
    protected static final String ACCESS_TOKEN = "sample.homework.token";

    protected final MockMvc mockMvc;

    protected AbstractWebMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Override
    public final ResultActions perform(RequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder);
    }

    public static DefaultMockMvcBuilder defaultMockMvcBuilder(WebApplicationContext context) {
        return MockMvcBuilders.webAppContextSetup(context);
    }

    public static StandaloneMockMvcBuilder standaloneMockMvcBuilder(ApplicationContext applicationContext) {
        Object[] controllers = findControllers(applicationContext);

        return MockMvcBuilders.standaloneSetup(controllers)
                              .setControllerAdvice(applicationContext.getBean(GlobalCustomException.class))
                              .addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true));
    }

    private static Object[] findControllers(ApplicationContext applicationContext) {
        return applicationContext.getBeansWithAnnotation(Controller.class)
                                 .values()
                                 .toArray();
    }
}
