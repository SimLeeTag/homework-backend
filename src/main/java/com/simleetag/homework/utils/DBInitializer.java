package com.simleetag.homework.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Profile("default")
@Configuration
@Transactional
@RequiredArgsConstructor
public class DBInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

    }
}
