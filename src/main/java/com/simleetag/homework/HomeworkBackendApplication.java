package com.simleetag.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class HomeworkBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeworkBackendApplication.class, args);
    }

}
