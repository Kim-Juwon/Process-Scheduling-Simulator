package com.pssimulator.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ProcessorRequestDto {
    @NotNull(message = "프로세서의 이름은 필수입니다.")
    private String name;

    @NotNull(message = "프로세서의 코어 정보는 필수입니다.")
    private CoreDto core;
}
