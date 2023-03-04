package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.ForbiddenException;

public class ReturnRegistrationRequestedOnReturnedItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "RETURN_REGISTRATION_REQUESTED_ON_RETURNED_ITEM";
    }

    @Override
    public String getMessage() {
        return "해당 물품은 이미 반납되어 있는 상태입니다. 확인 후 다시 시도해 주십시오.";
    }
}
