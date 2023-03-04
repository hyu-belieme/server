package com.example.beliemeserver.exception;

public class UnauthorizedException extends ServerException {
    public UnauthorizedException() {
        super(CommonErrorInfo.UNAUTHORIZED);
    }
}
