package com.simleetag.homework.api.config;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simleetag.homework.api.common.InvitationArgumentResolver;
import com.simleetag.homework.api.common.LoginUserArgumentResolver;
import com.simleetag.homework.api.domain.home.HomeJwt;
import com.simleetag.homework.api.domain.user.oauth.OAuthJwt;
import com.simleetag.homework.utils.JsonMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final OAuthJwt oauthJwt;

    private final HomeJwt homeJwt;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver(oauthJwt));
        resolvers.add(new InvitationArgumentResolver(homeJwt));
    }
}
