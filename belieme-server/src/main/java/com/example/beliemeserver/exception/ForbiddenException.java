package com.example.beliemeserver.exception;

public class ForbiddenException extends ServerException {
    public ForbiddenException() {
        super(CommonErrorInfo.FORBIDDEN);
    }
}
