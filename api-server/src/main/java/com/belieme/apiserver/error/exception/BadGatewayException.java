package com.belieme.apiserver.error.exception;

import com.belieme.apiserver.error.info.CommonErrorInfo;

public class BadGatewayException extends ServerException {

  public BadGatewayException() {
    super(CommonErrorInfo.BAD_GATEWAY);
  }
}
