package com.example.beliemeserver.exception;

public class NotFoundException extends ServerException {
    public NotFoundException() {
        super(CommonErrorInfo.NOT_FOUND);
    }
}
