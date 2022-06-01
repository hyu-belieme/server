package com.example.beliemeserver.model.exception;

public abstract class InternalServerException extends Exception {
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
