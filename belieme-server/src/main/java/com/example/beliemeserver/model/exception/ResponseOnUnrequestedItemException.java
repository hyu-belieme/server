package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.ForbiddenException;

public class ResponseOnUnrequestedItemException extends ForbiddenException {
    protected String koreanMessage() {
        return "현재 해당 물품에 대한 대여 신청이 존재하지 않습니다. 확인 후 다시 시도해 주십시오.";
    }
}
