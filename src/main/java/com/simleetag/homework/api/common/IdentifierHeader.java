package com.simleetag.homework.api.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IdentifierHeader {
    USER("x-user-id"),
    HOME("x-home-id");

    private final String key;
}
