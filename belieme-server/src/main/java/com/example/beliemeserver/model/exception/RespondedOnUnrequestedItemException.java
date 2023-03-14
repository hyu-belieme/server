package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.config.message.Message;
import com.example.beliemeserver.error.exception.ForbiddenException;

public class RespondedOnUnrequestedItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "RESPONDED_ON_UNREQUESTED_ITEM";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("error.respondedOnUnrequestedItem.message");
    }
}
