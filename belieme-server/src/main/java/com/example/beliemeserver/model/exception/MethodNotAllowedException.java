package com.example.beliemeserver.model.exception;

public class MethodNotAllowedException extends InternalServerException {
    public MethodNotAllowedException() {
    }

    public MethodNotAllowedException(String message) {
        super(message);
    }
}
