package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.ForbiddenException;

public class LostRegistrationRequestedOnLostItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "LOST_REGISTRATION_REQUESTED_ON_LOST_ITEM";
    }

    @Override
    public String getMessage() {
        return "이미 분실 등록이 된 물품입니다. 확인 후 다시 시도하여 주십시오.";
    }
}
