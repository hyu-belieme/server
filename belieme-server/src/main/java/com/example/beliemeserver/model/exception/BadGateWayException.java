package com.example.beliemeserver.model.exception;

public class BadGateWayException extends InternalServerException {
    public BadGateWayException() {
    }

    public BadGateWayException(String message) {
        super(message);
    }
}
