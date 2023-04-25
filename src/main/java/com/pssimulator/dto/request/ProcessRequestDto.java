package com.pssimulator.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class ProcessRequestDto {
    @NotBlank(message = "프로세스의 이름은 필수이며, 길이가 0이거나 공백 문자로만 이루어져 있으면 안됩니다.")
    private String name;

    @NotNull(message = "프로세스의 arrival time은 필수입니다.")
    private Integer arrivalTime;

    @NotNull(message = "프로세스의 workload는 필수입니다.")
    @Positive(message = "프로세스의 workload는 1 이상이어야 합니다.")
    private Integer workload;
}
