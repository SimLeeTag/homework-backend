package com.simleetag.homework.api.domain.home.api.dto;

import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeJwt;

public record CreatedHomeResponse(
        /**
         * 집 ID
         */
        @NotBlank
        Long homeId,

        /**
         * 집 이름
         */
        @NotBlank
        String homeName,

        /**
         * 집 초대링크 토큰
         */
        @NotBlank
        String invitation
) {
    public static CreatedHomeResponse from(Home home, HomeJwt homeJwt) {
        return new CreatedHomeResponse(home.getId(), home.getHomeName(), homeJwt.createHomeworkToken(home.getId()));
    }
}
