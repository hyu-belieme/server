package com.example.beliemeserver.exception;

public class BadGatewayException extends ServerException {
    public BadGatewayException() {
        super(CommonErrorInfo.BAD_GATEWAY);
    }
}
