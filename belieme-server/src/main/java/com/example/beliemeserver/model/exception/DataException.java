package com.example.beliemeserver.model.exception;

public abstract class DataException extends InternalServerException {
    public DataException() {
    }

    public DataException(String message) {
        super(message);
    }
}
