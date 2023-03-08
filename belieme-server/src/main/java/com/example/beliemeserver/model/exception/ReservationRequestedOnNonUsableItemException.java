package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.common.Message;
import com.example.beliemeserver.exception.ForbiddenException;

public class ReservationRequestedOnNonUsableItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "RESERVATION_REQUESTED_ON_NON_USABLE_ITEM";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("error.reservationRequestedOnNonUsableItem.message");
    }
}
