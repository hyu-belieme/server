package com.belieme.apiserver.domain.exception;

import com.belieme.apiserver.error.exception.ForbiddenException;
import com.belieme.apiserver.util.message.Message;

public class RespondedOnUnrequestedItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "RESPONDED_ON_UNREQUESTED_ITEM";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("message.error.respondedOnUnrequestedItem");
    }
}
