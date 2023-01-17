package com.example.beliemeserver.exception;

public class BadRequestException extends InternalServerException {
    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
}
