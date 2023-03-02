package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.ForbiddenException;

public class LostRegistrationOnLostItemException extends ForbiddenException {
    @Override
    protected String koreanMessage() {
        return "이미 분실 등록이 된 물품입니다. 확인 후 다시 시도하여 주십시오.";
    }
}
