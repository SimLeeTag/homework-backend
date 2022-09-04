package com.simleetag.homework.api.domain.home.api.dto;

import javax.validation.constraints.NotNull;

public record CreateHomeRequest(
        /**
         * 집 이름
         * <p>
         * Alert: 집 이름은 최대 12자이어야 합니다.
         */
        @NotNull
        String homeName
) {}
