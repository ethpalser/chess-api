package com.chess.api.core.exception;

import lombok.Getter;

@Getter
public class UnauthorizedAccessException extends Exception implements ErrorCode {

    private final String message;
    private final String errorCode;

    public UnauthorizedAccessException() {
        this("unauthorized access", "BAD-AUTH");
    }

    public UnauthorizedAccessException(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

}
