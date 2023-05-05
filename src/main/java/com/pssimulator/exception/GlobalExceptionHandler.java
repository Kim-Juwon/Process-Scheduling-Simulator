package com.pssimulator.exception;

import com.pssimulator.exception.response.ExceptionResponse;
import com.pssimulator.exception.response.RequestDataInvalidExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ExceptionResponse> internalServerError(InternalServerErrorException e) {
        return ResponseEntity.internalServerError()
                .body(ExceptionResponse.from(e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<RequestDataInvalidExceptionResponse> unprocessableEntity(BindException e) {
        List<String> messages = new ArrayList<>();

        e.getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            if (!messages.contains(message)) {
                messages.add(error.getDefaultMessage());
            }
        });

        return ResponseEntity.unprocessableEntity()
                .body(RequestDataInvalidExceptionResponse.from(messages));
    }
}
