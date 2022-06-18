package com.simleetag.homework.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simleetag.homework.HomeworkBackendApplication;
import com.simleetag.homework.api.utils.mockmvc.HttpMockMvc;
import com.simleetag.homework.api.utils.mockmvc.RestDocsMockMvcFactory;
import com.simleetag.homework.api.utils.mockmvc.WebMockMvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.core.env.Environment;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@SpringBootTest(classes = HomeworkBackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class IntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Environment environment;

    @Autowired
    protected ObjectMapper objectMapper;

    protected WebMockMvc successMockMvc;

    protected WebMockMvc failMockMvc;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider provider) {
        if (needDocumentation()) {
            this.successMockMvc = RestDocsMockMvcFactory.successRestDocsMockMvc(provider, wac);
            this.failMockMvc = RestDocsMockMvcFactory.failRestDocsMockMvc(provider, wac);
            return;
        }

        this.successMockMvc = HttpMockMvc.of(wac);
        this.failMockMvc = HttpMockMvc.of(wac);
    }

    private boolean needDocumentation() {
        return environment.getProperty("documentation-required", "false").equals("true");
    }
}
