package com.example.beliemeserver.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ServerException {
    @Override
    protected final HttpStatus httpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    protected String name() {
        return "BAD_REQUEST";
    }

    @Override
    protected String koreanMessage() {
        return "";
    }
}
