package com.example.beliemeserver.model.exception;

public class BadRequestException extends InternalServerException {
    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
}
