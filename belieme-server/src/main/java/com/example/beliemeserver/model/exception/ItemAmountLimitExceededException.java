package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.model.util.Constants;

public class ItemAmountLimitExceededException extends ForbiddenException {
    @Override
    protected String name() {
        return "ITEM_AMOUNT_LIMIT_EXCEEDED";
    }

    @Override
    protected String koreanMessage() {
        return "물품 당 Item 생성 제한에 도달했습니다.(제한 : " + Constants.MAX_ITEM_NUM + ")";
    }
}
