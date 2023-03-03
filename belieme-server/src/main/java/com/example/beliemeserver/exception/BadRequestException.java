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
        return "요청으로 전달 받은 변수들이 유효하지 않습니다.";
    }
}
