package com.example.beliemeserver.exception;

public class ForbiddenException extends InternalServerException {
    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
