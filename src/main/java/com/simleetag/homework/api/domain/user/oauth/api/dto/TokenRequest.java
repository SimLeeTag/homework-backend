package com.simleetag.homework.api.domain.user.oauth.api.dto;

import javax.validation.constraints.NotBlank;

import com.simleetag.homework.api.domain.user.oauth.ProviderType;

public record TokenRequest(
        /**
         * OAuth Provider로 부터 발급받은 AccessToken
         */
        @NotBlank
        String accessToken,

        /**
         * OAuth Provider 이름
         * <p>
         * Alert: ProviderType은 모두 [대문자]이어야 합니다
         */
        @NotBlank
        ProviderType providerType
) {}
