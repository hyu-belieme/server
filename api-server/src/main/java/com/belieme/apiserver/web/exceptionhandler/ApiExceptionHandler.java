package com.belieme.apiserver.web.exceptionhandler;

import com.belieme.apiserver.error.exception.ServerException;
import com.belieme.apiserver.error.info.CommonErrorInfo;
import com.belieme.apiserver.error.info.ErrorInfo;
import com.belieme.apiserver.util.message.Message;
import com.belieme.apiserver.web.responsebody.ExceptionResponse;
import java.util.List;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageSource messageSource;

  public ApiExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handleAllException(Exception e, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    ErrorInfo errorInfo = CommonErrorInfo.INTERNAL_SERVER_ERROR;
    ExceptionResponse errorBody = makeResponse(errorInfo);
    return handleExceptionInternal(e, errorBody, headers, errorInfo.httpStatus(), request);
  }

  @ExceptionHandler({ServerException.class})
  public ResponseEntity<Object> handleServerException(ServerException e, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    ExceptionResponse errorBody = makeResponse(e);
    return handleExceptionInternal(e, errorBody, headers, e.getHttpStatus(), request);
  }

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    ErrorInfo errorInfo = CommonErrorInfo.BAD_REQUEST;
    ExceptionResponse errorBody = makeResponse(errorInfo, makeValidationErrorResponse(e));
    return handleExceptionInternal(e, errorBody, headers, errorInfo.httpStatus(), request);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception e, @Nullable Object body,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    e.printStackTrace();
    if (body == null) {
      ErrorInfo errorInfo = CommonErrorInfo.getByHttpStatus(status);
      body = makeResponse(errorInfo);
      return super.handleExceptionInternal(e, body, headers, errorInfo.httpStatus(), request);
    }
    return super.handleExceptionInternal(e, body, headers, status, request);
  }

  private ExceptionResponse makeResponse(ServerException e) {
    return new ExceptionResponse(e.getName(), getMessageFromSource(e.getResponseMessage()));
  }

  private ExceptionResponse makeResponse(ErrorInfo errorInfo) {
    return new ExceptionResponse(errorInfo.name(),
        getMessageFromSource(errorInfo.responseMessage()));
  }

  private ExceptionResponse makeResponse(ErrorInfo errorInfo,
      List<ExceptionResponse.ValidationError> validationErrors) {
    return new ExceptionResponse(errorInfo.name(),
        getMessageFromSource(errorInfo.responseMessage()), validationErrors);
  }

  private List<ExceptionResponse.ValidationError> makeValidationErrorResponse(
      MethodArgumentNotValidException e) {
    return e.getBindingResult().getFieldErrors().stream().map(
        fieldError -> new ExceptionResponse.ValidationError(fieldError.getField(),
            getMessageFromSource(new Message(fieldError.getDefaultMessage())))).toList();
  }

  private String getMessageFromSource(Message message) {
    return messageSource.getMessage(message.getCode(), message.getArgs(),
        LocaleContextHolder.getLocale());
  }
}
