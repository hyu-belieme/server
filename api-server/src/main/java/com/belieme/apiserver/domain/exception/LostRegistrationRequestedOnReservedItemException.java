package com.belieme.apiserver.domain.exception;

import com.belieme.apiserver.error.exception.ForbiddenException;
import com.belieme.apiserver.util.message.Message;

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
