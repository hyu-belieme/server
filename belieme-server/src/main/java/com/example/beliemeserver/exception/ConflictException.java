package com.example.beliemeserver.exception;

public class ConflictException extends ServerException {
    public ConflictException() {
        super(CommonErrorInfo.CONFLICT);
    }
}
