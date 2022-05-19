package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.httpexception.HttpException;
import com.example.beliemeserver.controller.responsebody.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice  
@RestController 
public class Advice { 
    //TODO 최종적으로는 다음과 같은 ExceptionJsonBody를 response로 하기
    //     + 내 서버가 만든 Exception이라는 것을 알 수 있는 tag같은거 추가하기
    //     + 모든 Exception을 서버에 로그로 기록할 수 있게 추가
    //{
    //    "timestamp": "2021-03-24T14:18:39.140+0000",
    //    "status": 405,
    //    "error": "Method Not Allowed",
    //    "message": "Request method 'GET' not supported",
    //    "path": "/univs/HYU/users/2018008886/permissions"
    //}
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ExceptionResponse> exceptionResponse(HttpException e) {
        return e.toResponseEntity();
    }
}