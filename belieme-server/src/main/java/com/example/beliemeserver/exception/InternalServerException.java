package com.example.beliemeserver.exception;

public abstract class InternalServerException extends RuntimeException {
    private String message;

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

    public void setMessage(String message) {
        this.message = message;
    }
}
