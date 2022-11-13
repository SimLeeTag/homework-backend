package com.simleetag.homework.api.domain.home.api.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;
import com.simleetag.homework.api.domain.home.member.dto.MemberWithUserResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record HomeWithMembersResponse(
        @Schema(description = "집 ID")
        @NotBlank
        Long homeId,

        @Schema(description = "집 이름")
        @NotBlank
        String homeName,

        @Schema(description = "집에 속한 유저 목록")
        @NotBlank
        List<MemberWithUserResponse> members
) {
    public static HomeWithMembersResponse from(Home home, List<MemberWithUserResponse> memberWithUserRespons) {
        return new HomeWithMembersResponse(
                home.getId(),
                home.getHomeName(),
                memberWithUserRespons
        );
    }
}
