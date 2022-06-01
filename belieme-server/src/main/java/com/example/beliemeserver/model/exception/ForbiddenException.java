package com.example.beliemeserver.model.exception;

public class ForbiddenException extends InternalServerException {
    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
