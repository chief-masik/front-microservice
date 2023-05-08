package com.example.frontmicroservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.TimeoutException;

@Slf4j
//@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(JsonProcessingException.class)
    public void handleTimeoutException(JsonProcessingException e) {
        log.error(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public void handleUnexpectedException(Exception e) {
        log.error(e.getMessage());
    }

}
