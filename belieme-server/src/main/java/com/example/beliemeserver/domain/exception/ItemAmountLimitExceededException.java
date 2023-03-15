package com.example.beliemeserver.domain.exception;

import com.example.beliemeserver.config.message.Message;
import com.example.beliemeserver.domain.util.Constants;
import com.example.beliemeserver.error.exception.ForbiddenException;

public class ItemAmountLimitExceededException extends ForbiddenException {
    @Override
    public String getName() {
        return "ITEM_AMOUNT_LIMIT_EXCEEDED";
    }

    @Override
    public Message getResponseMessage() {
        Object[] args = {Constants.MAX_ITEM_NUM};
        return new Message("message.error.itemAmountLimitExceeded", args);
    }
}
