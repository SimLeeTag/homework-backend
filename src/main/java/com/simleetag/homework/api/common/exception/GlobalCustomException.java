package com.simleetag.homework.api.common.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalCustomException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception e) {
        final Error error = Error.from(e.getMessage());
        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> handleIllegalArgumentException(IllegalArgumentException e) {
        final Error error = Error.from(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<Error> handleJWTDecodeException() {
        final Error error = Error.from("잘못된 JWT 토큰입니다.");
        return ResponseEntity.badRequest().body(error);
    }
}
