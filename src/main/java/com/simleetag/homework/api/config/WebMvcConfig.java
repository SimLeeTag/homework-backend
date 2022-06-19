package com.simleetag.homework.api.config;

import java.util.List;

import com.simleetag.homework.api.common.LoginUserArgumentResolver;
import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final OAuthJwt oauthJwt;

    public WebMvcConfig(OAuthJwt oauthJwt) {
        this.oauthJwt = oauthJwt;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver(oauthJwt));
    }
}
