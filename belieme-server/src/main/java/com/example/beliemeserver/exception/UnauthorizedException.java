package com.example.beliemeserver.exception;

public class UnauthorizedException extends InternalServerException {
    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
