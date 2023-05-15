package com.example.beliemeserver.domain.exception;

import com.example.beliemeserver.util.message.Message;
import com.example.beliemeserver.error.exception.ForbiddenException;

public class LostRegistrationRequestedOnReservedItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "LOST_REGISTRATION_REQUESTED_ON_RESERVED_ITEM_EXCEPTION";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("message.error.lostRegistrationRequestedOnReservedItem");
    }
}