package com.example.beliemeserver.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ServerException {
    @Override
    protected HttpStatus httpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    protected String name() {
        return "NOT_FOUND";
    }

    @Override
    protected String koreanMessage() {
        return "요청된 리소스를 찾을 수 없습니다. 요청을 다시 한번 확인하여 주십시오.";
    }
}
