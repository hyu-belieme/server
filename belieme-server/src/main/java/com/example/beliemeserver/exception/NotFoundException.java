package com.example.beliemeserver.exception;

public class NotFoundException extends InternalServerException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
