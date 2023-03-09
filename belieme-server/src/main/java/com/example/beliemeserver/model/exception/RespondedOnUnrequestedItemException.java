package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.common.Message;
import com.example.beliemeserver.exception.ForbiddenException;

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
