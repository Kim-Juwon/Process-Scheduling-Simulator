package com.pssimulator.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "from")
public class ExceptionResponse {
    private final String message;
}
