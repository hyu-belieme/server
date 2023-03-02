package com.example.beliemeserver.exception;

public class UnauthorizedException extends InternalServerException {
    private String KR_MESSAGE() {
        return "액세스가 거부되었습니다. 유효한 토큰을 통해 접근해 주십시오.";
    }
    public UnauthorizedException() {
        setMessage(KR_MESSAGE());
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
