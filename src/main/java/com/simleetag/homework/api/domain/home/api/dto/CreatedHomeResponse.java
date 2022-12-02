package com.simleetag.homework.api.domain.home.api.dto;

import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.HomeJwt;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreatedHomeResponse(
        @Schema(description = "집 ID")
        @NotBlank
        Long homeId,

        @Schema(description = "집 이름")
        @NotBlank
        String homeName,

        @Schema(description = "집 초대링크 토큰")
        @NotBlank
        String invitation,

        @Schema(description = """
                집 초기화 여부
                                
                - true : 한 번이라도 집안일 설정을 완료함
                - false: 한 번도 집안일 설정을 완료하지 않음
                """)
        @NotBlank
                Boolean initialized
) {
    public static CreatedHomeResponse from(Home home, HomeJwt homeJwt) {
        return new CreatedHomeResponse(
                home.getId(),
                home.getHomeName(),
                homeJwt.createHomeworkToken(home.getId()),
                home.getInitialized());
    }
}
