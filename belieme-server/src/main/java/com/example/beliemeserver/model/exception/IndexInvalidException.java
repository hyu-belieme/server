package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.config.message.Message;
import com.example.beliemeserver.error.exception.BadRequestException;

public class IndexInvalidException extends BadRequestException {
    @Override
    public String getName() {
        return "INDEX_INVALID";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("message.error.indexInvalid");
    }
}
