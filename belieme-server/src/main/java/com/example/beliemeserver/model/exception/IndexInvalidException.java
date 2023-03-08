package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.common.Message;
import com.example.beliemeserver.exception.BadRequestException;

public class IndexInvalidException extends BadRequestException {
    @Override
    public String getName() {
        return "INDEX_INVALID";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("error.indexInvalid.message");
    }
}
