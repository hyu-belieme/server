package com.example.beliemeserver.model.exception;

public class UnauthorizedException extends InternalServerException {
    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
