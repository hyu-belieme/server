package com.belieme.apiserver.error.exception;

import com.belieme.apiserver.error.info.CommonErrorInfo;

public class UnauthorizedException extends ServerException {

  public UnauthorizedException() {
    super(CommonErrorInfo.UNAUTHORIZED);
  }
}
