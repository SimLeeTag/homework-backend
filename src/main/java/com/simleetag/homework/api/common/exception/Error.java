package com.simleetag.homework.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Error {

    private String message;

    public static Error from(String message) {
        return new Error(message);
    }
}
