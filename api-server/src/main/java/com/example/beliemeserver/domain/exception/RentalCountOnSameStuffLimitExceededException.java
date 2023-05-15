package com.example.beliemeserver.domain.exception;

import com.example.beliemeserver.util.message.Message;
import com.example.beliemeserver.domain.util.Constants;
import com.example.beliemeserver.error.exception.ForbiddenException;

public class RentalCountOnSameStuffLimitExceededException extends ForbiddenException {
    @Override
    public String getName() {
        return "RENTAL_COUNT_ON_SAME_STUFF_LIMIT_EXCEEDED";
    }

    @Override
    public Message getResponseMessage() {
        Object[] args = {Constants.MAX_RENTAL_COUNT_ON_SAME_STUFF};
        return new Message("message.error.rentalCountOnSameStuffLimitExceeded", args);
    }
}
