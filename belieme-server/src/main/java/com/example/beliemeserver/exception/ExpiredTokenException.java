package com.example.beliemeserver.exception;

public class ExpiredTokenException extends InternalServerException {
    public ExpiredTokenException() {
    }

    public ExpiredTokenException(String message) {
        super(message);
    }
}
