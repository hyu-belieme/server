package com.example.beliemeserver.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends ServerException {
    @Override
    protected final HttpStatus httpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    protected String name() {
        return "INTERNAL_SERVER_ERROR";
    }

    @Override
    protected String koreanMessage() {
        return "예상하지 못한 문제가 서버 내부에 발생하였습니다. " +
                "나중에 다시 시도하여 주십시오.";
    }
}
