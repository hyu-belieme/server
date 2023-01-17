package com.example.beliemeserver.model.exception.old;

import com.example.beliemeserver.exception.InternalServerException;

public abstract class DataException extends InternalServerException {
    public DataException() {
    }

    public DataException(String message) {
        super(message);
    }
}
