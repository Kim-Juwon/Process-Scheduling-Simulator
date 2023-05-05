package com.pssimulator.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(staticName = "from")
public class RequestDataInvalidExceptionResponse {
    private final List<String> messages;
}
