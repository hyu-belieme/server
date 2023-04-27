package com.example.beliemeserver.domain.exception;

import com.example.beliemeserver.util.message.Message;
import com.example.beliemeserver.error.exception.ForbiddenException;

public class UsableItemNotExistedException extends ForbiddenException {
    @Override
    public String getName() {
        return "USABLE_ITEM_NOT_EXISTED";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("message.error.usableItemNotExisted");
    }
}
