package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.ForbiddenException;

public class UsableItemNotExistedException extends ForbiddenException {
    @Override
    public String getName() {
        return "USABLE_ITEM_NOT_EXISTED";
    }

    @Override
    public String getMessage() {
        return "현재 대여 가능한 물품이 존재하지 않습니다. 나중에 다시 시도하여 주십시오.";
    }
}
