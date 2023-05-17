package com.belieme.apiserver.domain.exception;

import com.belieme.apiserver.error.exception.ForbiddenException;
import com.belieme.apiserver.util.message.Message;

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
