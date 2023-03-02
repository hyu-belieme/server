package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.ForbiddenException;

public class ReservationOnNonUsableItemException extends ForbiddenException {
    @Override
    protected String koreanMessage() {
        return "해당 물품은 현재 대여가능한 상태가 아닙니다. 확인 후 다시 시도해 주십시오.";
    }
}
