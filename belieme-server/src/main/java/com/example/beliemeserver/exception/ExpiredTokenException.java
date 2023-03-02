package com.example.beliemeserver.exception;

public class ExpiredTokenException extends ServerException {
    @Override
    protected String koreanMessage() {
        return "액세스가 거부되었습니다. 해당 토큰이 만료되었습니다.";
    }
}