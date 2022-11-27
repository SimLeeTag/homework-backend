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

        @Schema(description = """
                집 초기화 여부
                
                - true : 한 번이라도 집안일 설정을 완료함
                - false: 한 번도 집안일 설정을 완료하지 않음
                """)
        @NotBlank
        Boolean initialized,

        @Schema(description = "집에 속한 유저 목록")
        @NotBlank
        List<MemberWithUserResponse> members
) {
    public static HomeWithMembersResponse from(Home home) {
        return new HomeWithMembersResponse(
                home.getId(),
                home.getHomeName(),
                home.getInitialized(),
                MemberWithUserResponse.from(home.getMembers())
        );
    }

    public static List<HomeWithMembersResponse> from(List<Home> homes) {
        return homes.stream().map(HomeWithMembersResponse::from).toList();
    }
}
