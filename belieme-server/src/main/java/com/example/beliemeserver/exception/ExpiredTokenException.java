package com.example.beliemeserver.exception;

public class ExpiredTokenException extends InternalServerException {
    private static final String KR_MESSAGE = "해당 토큰이 만료되었습니다.";

    public ExpiredTokenException() {
        setMessage(KR_MESSAGE);
    }
}