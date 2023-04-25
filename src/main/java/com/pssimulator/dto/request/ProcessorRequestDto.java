package com.pssimulator.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ProcessorRequestDto {
    @NotBlank(message = "프로세서의 이름은 필수이며, 길이가 0이거나 공백 문자로만 이루어져 있으면 안됩니다.")
    private String name;

    @NotNull(message = "프로세서의 코어 정보는 필수입니다.")
    private CoreDto core;
}
