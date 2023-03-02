package com.example.beliemeserver.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ServerException {
    @Override
    protected final HttpStatus httpStatus() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    protected String name() {
        return "FORBIDDEN";
    }

    protected String koreanMessage() {
        return "요청이 거부되었습니다.";
    }
}
