package com.simleetag.homework.api.common.exception;

public record Error(String message) {
    public static Error from(String message) {
        return new Error(message);
    }
}
