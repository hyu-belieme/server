package com.example.beliemeserver.exception;

public class BadRequestException extends ServerException {
    public BadRequestException() {
        super(CommonErrorInfo.BAD_REQUEST);
    }
}
