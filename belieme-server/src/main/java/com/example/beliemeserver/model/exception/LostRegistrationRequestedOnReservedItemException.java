package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.config.message.Message;
import com.example.beliemeserver.error.exception.ForbiddenException;

public class LostRegistrationRequestedOnReservedItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "LOST_REGISTRATION_REQUESTED_ON_RESERVED_ITEM_EXCEPTION";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("error.lostRegistrationRequestedOnReservedItem.message");
    }
}
