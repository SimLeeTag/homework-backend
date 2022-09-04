package com.simleetag.homework.api.domain.user.oauth.api.dto;

import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.user.oauth.ProviderType;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenRequest(
        @Schema(description = "OAuth Provider로 부터 발급받은 AccessToken")
        @NotBlank
        String accessToken,

        @Schema(
                description = "OAuth Provider 이름",
                oneOf = ProviderType.class
        )
        @NotBlank
        ProviderType providerType
) {}
