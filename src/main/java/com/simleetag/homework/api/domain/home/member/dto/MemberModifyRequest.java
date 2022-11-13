package com.simleetag.homework.api.domain.home.member.dto;

import javax.validation.constraints.PositiveOrZero;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberModifyRequest(
        @Schema(description = "멤버가 가진 포인트")
        @PositiveOrZero
        Integer point
) {
}
