package com.simleetag.homework.api.common;

import com.simleetag.homework.HomeworkBackendApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.TestInstance;

@ActiveProfiles("embedded")
@ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
@SpringBootTest(classes = HomeworkBackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TestSupport {

    @Autowired
    protected MockMvc mockMvc;
}
