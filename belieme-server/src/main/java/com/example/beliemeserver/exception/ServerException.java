package com.example.beliemeserver.exception;

import org.springframework.http.HttpStatus;

public abstract class ServerException extends RuntimeException {
    public HttpStatus getHttpStatus() {
        return httpStatus();
    }

    public String getName() {
        return name();
    }

    @Override
    public String getMessage() {
        return koreanMessage();
    }

    protected abstract HttpStatus httpStatus();

    protected abstract String name();

    protected abstract String koreanMessage();
}
