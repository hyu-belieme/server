package com.example.beliemeserver.exception;

import org.springframework.http.HttpStatus;

public abstract class ServerException extends RuntimeException {
    private final ErrorInfo errorInfo;

    public ServerException(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    public final HttpStatus getHttpStatus() {
        return errorInfo.httpStatus();
    }

    public String getName() {
        return errorInfo.name();
    }

    @Override
    public String getMessage() {
        return errorInfo.responseMessage();
    }
}
