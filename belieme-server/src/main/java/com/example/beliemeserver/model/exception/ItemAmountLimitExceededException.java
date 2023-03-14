package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.config.message.Message;
import com.example.beliemeserver.error.exception.ForbiddenException;
import com.example.beliemeserver.model.util.Constants;

public class ItemAmountLimitExceededException extends ForbiddenException {
    @Override
    public String getName() {
        return "ITEM_AMOUNT_LIMIT_EXCEEDED";
    }

    @Override
    public Message getResponseMessage() {
        Object[] args = {Constants.MAX_ITEM_NUM};
        return new Message("error.itemAmountLimitExceeded.message", args);
    }
}
