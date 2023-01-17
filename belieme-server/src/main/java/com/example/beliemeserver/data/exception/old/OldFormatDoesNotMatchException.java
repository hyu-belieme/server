package com.example.beliemeserver.data.exception.old;

import com.example.beliemeserver.model.exception.old.DataException;

public class OldFormatDoesNotMatchException extends DataException {
    public OldFormatDoesNotMatchException() {
    }

    public OldFormatDoesNotMatchException(String message) {
        super(message);
    }
}
