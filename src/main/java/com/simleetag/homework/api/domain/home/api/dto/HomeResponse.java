package com.simleetag.homework.api.domain.home.api.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.user.api.dto.MemberResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record HomeResponse(
        @Schema(description = "집 ID")
        @NotBlank
        Long homeId,

        @Schema(description = "집 이름")
        @NotBlank
        String homeName,

        @Schema(description = "집에 속한 유저 목록")
        @NotBlank
        List<MemberResponse> members
) {
    public static List<HomeResponse> from(List<Home> homes) {
        List<HomeResponse> homeResponses = new ArrayList<>();
        for (Home home : homes) {
            homeResponses.add(
                    new HomeResponse(
                            home.getId(),
                            home.getHomeName(),
                            MemberResponse.from(home.getMembers())
                    ));
        }

        return homeResponses;
    }

    public static HomeResponse from(Home home) {
        return new HomeResponse(home.getId(),
                home.getHomeName(),
                MemberResponse.from(home.getMembers())
        );
    }
}
