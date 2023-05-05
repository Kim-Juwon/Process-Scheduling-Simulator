package com.pssimulator.exception;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(ExceptionMessages exceptionMessages) {
        super(exceptionMessages.getMessage());
    }
}
