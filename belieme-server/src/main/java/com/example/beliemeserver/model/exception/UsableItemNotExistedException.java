package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.common.Message;
import com.example.beliemeserver.exception.ForbiddenException;

public class UsableItemNotExistedException extends ForbiddenException {
    @Override
    public String getName() {
        return "USABLE_ITEM_NOT_EXISTED";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("error.usableItemNotExisted.message");
    }
}
