package com.example.beliemeserver.exception;

public class BadGateWayException extends InternalServerException {
    public BadGateWayException() {
    }

    public BadGateWayException(String message) {
        super(message);
    }
}
