package com.belieme.apiserver.domain.exception;

import com.belieme.apiserver.error.exception.ForbiddenException;
import com.belieme.apiserver.util.message.Message;
import com.belieme.apiserver.domain.util.Constants;

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
