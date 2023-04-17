package com.example.beliemeserver.domain.exception;

import com.example.beliemeserver.util.message.Message;
import com.example.beliemeserver.error.exception.ForbiddenException;

public class LostRegistrationRequestedOnLostItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "LOST_REGISTRATION_REQUESTED_ON_LOST_ITEM";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("message.error.lostRegistrationRequestedOnLostItem");
    }
}
