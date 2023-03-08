package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.common.Message;
import com.example.beliemeserver.exception.ForbiddenException;

public class LostRegistrationRequestedOnLostItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "LOST_REGISTRATION_REQUESTED_ON_LOST_ITEM";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("error.lostRegistrationRequestedOnLostItem.message");
    }
}
