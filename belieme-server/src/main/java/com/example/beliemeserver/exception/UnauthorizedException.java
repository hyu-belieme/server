package com.example.beliemeserver.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ServerException {
    @Override
    protected HttpStatus httpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    protected String name() {
        return "UNAUTHORIZED";
    }

    @Override
    protected String koreanMessage() {
        return "토큰 인증에 실패하였습니다. 유효한 토큰을 통해 접근해 주십시오.";
    }
}
