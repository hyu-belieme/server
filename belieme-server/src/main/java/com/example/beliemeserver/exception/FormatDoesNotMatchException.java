package com.example.beliemeserver.exception;

public class FormatDoesNotMatchException extends InternalServerException {
    public FormatDoesNotMatchException() {
    }

    public FormatDoesNotMatchException(String message) {
        super(message);
    }
}
