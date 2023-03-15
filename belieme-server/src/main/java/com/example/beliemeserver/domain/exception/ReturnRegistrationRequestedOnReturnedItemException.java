package com.example.beliemeserver.domain.exception;

import com.example.beliemeserver.config.message.Message;
import com.example.beliemeserver.error.exception.ForbiddenException;

public class ReturnRegistrationRequestedOnReturnedItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "RETURN_REGISTRATION_REQUESTED_ON_RETURNED_ITEM";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("message.error.returnRegistrationRequestedOnReturnedItem");
    }
}
