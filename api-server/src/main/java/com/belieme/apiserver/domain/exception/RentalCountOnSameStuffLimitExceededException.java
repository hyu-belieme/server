package com.belieme.apiserver.domain.exception;

import com.belieme.apiserver.domain.util.Constants;
import com.belieme.apiserver.error.exception.ForbiddenException;
import com.belieme.apiserver.util.message.Message;

public class RentalCountOnSameStuffLimitExceededException extends ForbiddenException {

  @Override
  public String getName() {
    return "RENTAL_COUNT_ON_SAME_STUFF_LIMIT_EXCEEDED";
  }

  @Override
  public Message getResponseMessage() {
    Object[] args = {Constants.MAX_RENTAL_COUNT_ON_SAME_STUFF};
    return new Message("message.error.rentalCountOnSameStuffLimitExceeded", args);
  }
}
