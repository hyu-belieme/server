package com.example.beliemeserver.web.exception;

import com.example.beliemeserver.error.exception.BadRequestException;
import com.example.beliemeserver.util.message.Message;

public class InvalidUuidFormatException extends BadRequestException {
    @Override
    public String getName() {
        return "INVALID_UUID_FORMAT";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("message.error.invalidUuidFormat");
    }
}
