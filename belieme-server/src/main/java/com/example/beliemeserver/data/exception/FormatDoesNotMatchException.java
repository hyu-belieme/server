package com.example.beliemeserver.data.exception;

import com.example.beliemeserver.model.exception.DataException;

public class FormatDoesNotMatchException extends DataException {
    public FormatDoesNotMatchException() {
    }

    public FormatDoesNotMatchException(String message) {
        super(message);
    }
}
