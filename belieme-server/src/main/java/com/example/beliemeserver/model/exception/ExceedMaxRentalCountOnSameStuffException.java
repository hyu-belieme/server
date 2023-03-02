package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.model.util.Constants;

public class ExceedMaxRentalCountOnSameStuffException extends ForbiddenException {
    @Override
    protected String koreanMessage() {
        return "특정 물품에 대한 대여 한도에 도달했습니다.(제한 : " + Constants.MAX_RENTAL_COUNT_ON_SAME_STUFF + ")";
    }
}
