package com.pssimulator.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ExceptionResponse> internalServerError(InternalServerErrorException e) {
        return ResponseEntity.internalServerError()
                .body(ExceptionResponse.from(e.getMessage()));
    }
}
