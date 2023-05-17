package com.belieme.apiserver.error.info;

import com.belieme.apiserver.util.message.Message;
import org.springframework.http.HttpStatusCode;

public interface ErrorInfo {

  String name();

  HttpStatusCode httpStatus();

  Message responseMessage();
}
