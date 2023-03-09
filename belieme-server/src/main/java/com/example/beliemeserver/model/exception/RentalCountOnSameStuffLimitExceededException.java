package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.common.Message;
import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.model.util.Constants;

public class RentalCountOnSameStuffLimitExceededException extends ForbiddenException {
    @Override
    public String getName() {
        return "RENTAL_COUNT_ON_SAME_STUFF_LIMIT_EXCEEDED";
    }

    @Override
    public Message getResponseMessage() {
        Object[] args = {Constants.MAX_RENTAL_COUNT_ON_SAME_STUFF};
        return new Message("error.rentalCountOnSameStuffLimitExceeded.message", args);
    }
}
