package com.simleetag.homework.api.common;

import java.util.Enumeration;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import com.simleetag.homework.api.domain.user.oauth.OAuthJwt;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final OAuthJwt oauthJwt;

    public LoginUserArgumentResolver(OAuthJwt oauthJwt) {
        this.oauthJwt = oauthJwt;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class)
                && Long.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class));
        final Enumeration<String> userIds = request.getHeaders(IdentifierHeader.USER.getKey());
        if (!userIds.hasMoreElements()) {
            throw new IllegalArgumentException("USER ID 헤더를 입력하지 않았습니다.");
        }

        final String userIdJwt = userIds.nextElement();
        if (userIdJwt.isBlank()) {
            throw new IllegalArgumentException("USER ID 헤더가 비어있습니다.");
        }

        return oauthJwt.parseClaimsAsUserId(userIdJwt);
    }
}
