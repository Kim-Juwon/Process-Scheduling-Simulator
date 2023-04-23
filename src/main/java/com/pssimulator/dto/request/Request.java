package com.pssimulator.dto.request;

import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Request {
    @Valid
    @NotEmpty(message = "프로세스는 최소 1개 있어야 합니다.")
    @Size(max = 99, message = "프로세스는 최대 99개입니다.")
    private List<ProcessRequestDto> processes = new ArrayList<>();

    @Valid
    @NotEmpty(message = "프로세서는 최소 1개 있어야 합니다.")
    @Size(max = 4, message = "프로세서는 최대 4개입니다.")
    private List<ProcessorRequestDto> processors = new ArrayList<>();

    @NotNull(message = "스케줄링 알고리즘은 필수입니다.")
    private AlgorithmDto algorithm;

    @NotNull(message = "time quantum은 필수입니다.")
    private Integer timeQuantum;
}
