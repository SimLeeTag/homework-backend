package com.simleetag.homework.api.domain.home.dto;

import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.infra.HomeJwt;

import lombok.Getter;

@Getter
public class CreatedHomeResponse {

    /**
     * 집 ID
     */
    @NotBlank
    private final Long homeId;

    /**
     * 집 이름
     */
    @NotBlank
    private final String homeName;

    /**
     * 집 초대링크 토큰
     */
    @NotBlank
    private String invitation;

    public CreatedHomeResponse(Home home, HomeJwt homeJwt) {
        this.homeId = home.getId();
        this.homeName = home.getHomeName();
        this.invitation = homeJwt.createHomeworkToken(home.getId());
    }

    public static CreatedHomeResponse from(Home home, HomeJwt homeJwt) {
        return new CreatedHomeResponse(home, homeJwt);
    }

}
