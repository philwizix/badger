package com.philiprushby.techassessment.exception;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ClientExceptionAdvice {

  @ResponseBody
  @ExceptionHandler(ClientException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String clientExceptionHandler(ClientException ex) {
    return ex.getMessage();
  }
}