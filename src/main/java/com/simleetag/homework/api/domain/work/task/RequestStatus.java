package com.simleetag.homework.api.domain.work.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestStatus {
    NONE("나의 집안일"),
    REQUESTING("부탁한 집안일"),
    REQUESTED("부탁받은 집안일");

    private final String description;
}
