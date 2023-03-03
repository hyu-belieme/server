package com.example.beliemeserver.exception;

public class InternalServerException extends ServerException {
    public InternalServerException() {
        super(CommonErrorInfo.INTERNAL_SERVER_ERROR);
    }
}
