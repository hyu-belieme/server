package com.example.beliemeserver.exception;

public class InvalidIndexException extends InternalServerException {
    public InvalidIndexException() {
        super();
    }

    public InvalidIndexException(String message) {
        super(message);
    }
}
