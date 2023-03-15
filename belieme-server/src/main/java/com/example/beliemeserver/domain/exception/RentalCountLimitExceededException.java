package com.example.beliemeserver.domain.exception;

import com.example.beliemeserver.config.message.Message;
import com.example.beliemeserver.domain.util.Constants;
import com.example.beliemeserver.error.exception.ForbiddenException;

public class RentalCountLimitExceededException extends ForbiddenException {
    @Override
    public String getName() {
        return "RENTAL_COUNT_LIMIT_EXCEEDED";
    }

    @Override
    public Message getResponseMessage() {
        Object[] args = {Constants.MAX_RENTAL_COUNT};
        return new Message("message.error.rentalCountLimitExceeded", args);
    }
}
