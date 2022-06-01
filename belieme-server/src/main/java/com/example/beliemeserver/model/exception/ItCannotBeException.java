package com.example.beliemeserver.model.exception;

public class ItCannotBeException extends InternalServerException {
    public ItCannotBeException() {
    }

    public ItCannotBeException(String message) {
        super(message);
    }
}
