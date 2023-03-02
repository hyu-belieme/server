package com.example.beliemeserver.exception;

public class ForbiddenException extends ServerException {
    protected String koreanMessage() {
        return "요청이 거부되었습니다.";
    }
}
