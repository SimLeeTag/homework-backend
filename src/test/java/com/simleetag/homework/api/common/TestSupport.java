package com.simleetag.homework.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simleetag.homework.HomeworkBackendApplication;
import com.simleetag.homework.api.common.mockmvc.RestDocsMockMvcFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@Transactional
@ExtendWith({RestDocumentationExtension.class})
@ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@SpringBootTest(classes = HomeworkBackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class TestSupport {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc defaultMockMvc;
    /**
     * @BeforeEach 가 수행되고 나서 값이 할당되므로,
     * @BeforeAll 을 사용할 경우 해당 변수를 @BeforeEach에서 사용하면 안된다.
     */
    protected MockMvc restDocsMockMvc;
    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider provider) {
        this.restDocsMockMvc = RestDocsMockMvcFactory.createMockMvc(provider, wac);
    }
}
