package com.simleetag.homework.api.domain.home.api.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public record EmptyHomeCreateRequest(
        @Schema(
                description = "집 이름",
                maxLength = 12
        )
        @NotNull
        String homeName
) {}
