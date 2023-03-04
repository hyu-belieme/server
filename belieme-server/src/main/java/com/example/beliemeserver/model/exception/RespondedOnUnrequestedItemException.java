package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.ForbiddenException;

public class RespondedOnUnrequestedItemException extends ForbiddenException {
    @Override
    public String getName() {
        return "RESPONDED_ON_UNREQUESTED_ITEM";
    }

    @Override
    public String getMessage() {
        return "현재 해당 물품에 대한 대여 신청이 존재하지 않습니다. 확인 후 다시 시도해 주십시오.";
    }
}
