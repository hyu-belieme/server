package com.example.beliemeserver.domain.exception;

import com.example.beliemeserver.util.message.Message;
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
