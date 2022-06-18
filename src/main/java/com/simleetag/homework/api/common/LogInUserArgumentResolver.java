package com.simleetag.homework.api.common;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import com.simleetag.homework.api.domain.oauth.infra.OAuthJwt;
import com.simleetag.homework.api.domain.user.LogInUser;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LogInUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final OAuthJwt oauthJwt;

    public LogInUserArgumentResolver(OAuthJwt oauthJwt) {
        this.oauthJwt = oauthJwt;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LogIn.class)
                && LogInUser.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class));
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader.startsWith("Bearer")) {
            final String accessToken = authorizationHeader.split(" ")[1];
            return oauthJwt.parseClaimsAsLoginUser(accessToken);
        }

        throw new IllegalArgumentException("Bearer 형식의 토큰이 아닙니다.");
    }
}
