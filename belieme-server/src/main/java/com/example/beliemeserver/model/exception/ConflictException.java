package com.example.beliemeserver.model.exception;

public class ConflictException extends InternalServerException {
    public ConflictException() {
    }

    public ConflictException(String message) {
        super(message);
    }
}
