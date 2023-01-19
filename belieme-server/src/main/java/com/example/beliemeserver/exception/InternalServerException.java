package com.example.beliemeserver.exception;

public abstract class InternalServerException extends RuntimeException {
    String message;

    public InternalServerException() {
        message = "no error message";
    }

    public InternalServerException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
