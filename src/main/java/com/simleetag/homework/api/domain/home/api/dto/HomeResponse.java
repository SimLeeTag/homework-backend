package com.simleetag.homework.api.domain.home.api.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.home.Home;

import io.swagger.v3.oas.annotations.media.Schema;

public record HomeResponse(
        @Schema(description = "집 ID")
        @NotBlank
        Long homeId,

        @Schema(description = "집 이름")
        @NotBlank
        String homeName,

        @Schema(description = "집 생성 시각")
        LocalDateTime createdAt,

        @Schema(description = "집 삭제 시각")
        LocalDateTime deletedAt,

        @Schema(description = """
                집 초기화 여부
                                
                - true : 한 번이라도 집안일 설정을 완료함
                - false: 한 번도 집안일 설정을 완료하지 않음
                """)
        @NotBlank
        Boolean initialized
) {
    public static HomeResponse from(Home home) {
        return new HomeResponse(
                home.getId(),
                home.getHomeName(),
                home.getCreatedAt(),
                home.getDeletedAt(),
                home.getInitialized());
    }
}
