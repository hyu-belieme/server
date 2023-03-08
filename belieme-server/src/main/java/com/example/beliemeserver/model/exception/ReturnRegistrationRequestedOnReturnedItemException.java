package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.common.Message;
import com.example.beliemeserver.exception.ForbiddenException;

public class ReturnRegistrationRequestedOnReturnedItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "RETURN_REGISTRATION_REQUESTED_ON_RETURNED_ITEM";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("error.returnRegistrationRequestedOnReturnedItem.message");
    }
}
