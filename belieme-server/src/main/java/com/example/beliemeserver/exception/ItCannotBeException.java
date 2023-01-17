package com.example.beliemeserver.exception;

public class ItCannotBeException extends InternalServerException {
    public ItCannotBeException() {
    }

    public ItCannotBeException(String message) {
        super(message);
    }
}
