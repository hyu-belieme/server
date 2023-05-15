package com.belieme.apiserver.domain.exception;

import com.belieme.apiserver.error.exception.ForbiddenException;
import com.belieme.apiserver.util.message.Message;

public class ReservationRequestedOnNonUsableItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "RESERVATION_REQUESTED_ON_NON_USABLE_ITEM";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("message.error.reservationRequestedOnNonUsableItem");
    }
}
