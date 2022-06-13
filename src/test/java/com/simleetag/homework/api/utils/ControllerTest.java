package com.simleetag.homework.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simleetag.homework.api.domain.oauth.OAuthService;
import com.simleetag.homework.api.domain.user.UserService;
import com.simleetag.homework.api.utils.mockmvc.HttpMockMvc;
import com.simleetag.homework.api.utils.mockmvc.RestDocsMockMvcFactory;
import com.simleetag.homework.api.utils.mockmvc.WebMockMvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@WebMvcTest(excludeAutoConfiguration = AutoConfigureMockMvc.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public abstract class ControllerTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;

    @Autowired
    protected ObjectMapper objectMapper;

    protected WebMockMvc successMockMvc;

    protected WebMockMvc failMockMvc;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider provider) {
        if (needDocumentation()) {
            this.successMockMvc = RestDocsMockMvcFactory.successRestDocsMockMvc(provider, applicationContext);
            this.failMockMvc = RestDocsMockMvcFactory.failRestDocsMockMvc(provider, applicationContext);
            return;
        }

        this.successMockMvc = HttpMockMvc.of(applicationContext);
        this.failMockMvc = HttpMockMvc.of(applicationContext);
    }

    private boolean needDocumentation() {
        return environment.getProperty("documentation-required", "false").equals("true");
    }

    @MockBean
    protected OAuthService oAuthService;

    @MockBean
    protected UserService userService;
}
