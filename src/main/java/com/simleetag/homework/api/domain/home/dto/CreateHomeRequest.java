package com.simleetag.homework.api.domain.home.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateHomeRequest {

    /**
     * 집 이름
     * <p>
     * Alert: 집 이름은 최대 12자이어야 합니다.
     */
    @NotNull
    private String homeName;
}
