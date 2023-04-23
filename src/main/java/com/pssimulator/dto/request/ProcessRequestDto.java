package com.pssimulator.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ProcessRequestDto {
    @NotNull(message = "프로세스의 이름은 필수입니다.")
    private String name;

    @NotNull(message = "프로세스의 arrival time은 필수입니다.")
    private Integer arrivalTime;

    @NotNull(message = "프로세스의 workload는 필수입니다.")
    private Integer workload;
}
