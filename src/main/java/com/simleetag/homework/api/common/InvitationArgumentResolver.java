package com.simleetag.homework.api.common;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

import com.simleetag.homework.api.common.exception.CustomHeaderException;
import com.simleetag.homework.api.domain.home.HomeJwt;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class InvitationArgumentResolver implements HandlerMethodArgumentResolver {
    private final HomeJwt homeJwt;

    public InvitationArgumentResolver(HomeJwt homeJwt) {
        this.homeJwt = homeJwt;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Invitation.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class));
        String invitationToken = request.getHeader("invitation");
        if (!invitationToken.isEmpty()) {
            return homeJwt.parseClaimsAsHomeId(invitationToken);
        }
        throw new CustomHeaderException("invitation header의 값이 비어있습니다.");
    }
}
