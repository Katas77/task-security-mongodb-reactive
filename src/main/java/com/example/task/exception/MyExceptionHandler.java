package com.example.task.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleExceptionNull(NullPointerException e) {
        log.error("NullPointerException occurred: {}", (Object) e.getStackTrace());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(MyException.class)
    public ResponseEntity<String> EntityNotFoundException(MyException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }


}




