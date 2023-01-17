package com.example.beliemeserver.exception;

public class MethodNotAllowedException extends InternalServerException {
    public MethodNotAllowedException() {
    }

    public MethodNotAllowedException(String message) {
        super(message);
    }
}
