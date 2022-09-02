package com.simleetag.homework;

import com.simleetag.homework.api.config.AppEnvironment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication
@EnableConfigurationProperties(AppEnvironment.class)
@ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
public class HomeworkBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeworkBackendApplication.class, args);
    }

}
