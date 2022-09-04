package com.simleetag.homework.api.common.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Error> handleAuthenticationException(AuthenticationException e) {
        final Error error = Error.from(e.getMessage());
        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(OAuthException.class)
    public ResponseEntity<Error> handleOAuthException(OAuthException e) {
        final Error error = Error.from(e.getMessage());
        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Error> handleWebClientResponseException(WebClientResponseException e) {
        final Error error = Error.from(e.getResponseBodyAsString());
        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<Error> handleJWTDecodeException() {
        final Error error = Error.from("잘못된 JWT 토큰입니다.");
        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(HomeJoinException.class)
    public ResponseEntity<Error> handleHomeJoinException(HomeJoinException e) {
        final Error error = Error.from(e.getMessage());
        return ResponseEntity.status(400).body(error);
    }
}
