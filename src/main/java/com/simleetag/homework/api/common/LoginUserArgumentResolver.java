package com.simleetag.homework.api.common;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import com.simleetag.homework.api.common.exception.AuthenticationException;
import com.simleetag.homework.api.domain.user.LoginUser;
import com.simleetag.homework.api.domain.user.oauth.OAuthJwt;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
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
                && LoginUser.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class));
        String[] authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ");
        String authType = authorizationHeader[0].toLowerCase();
        if (authType.startsWith("bearer")) {
            final String homeworkToken = authorizationHeader[1];
            return oauthJwt.parseClaimsAsLoginUser(homeworkToken);
        }

        throw new AuthenticationException("Bearer 형식의 토큰이 아닙니다.");
    }
}
