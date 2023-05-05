package com.pssimulator.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessages {
    ALGORITHM_NOT_FOUND("알고리즘이 존재하지 않습니다."),
    CORE_NOT_FOUND("코어가 존재하지 않습니다.");

    ExceptionMessages(String message) {
        this.message = message;
    }

    private final String message;
}
