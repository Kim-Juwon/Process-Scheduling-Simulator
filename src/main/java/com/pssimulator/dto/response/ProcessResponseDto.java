package com.pssimulator.dto.response;

import com.pssimulator.domain.process.Process;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class ProcessResponseDto {
    private final String name;
    private final Integer arrivalTime;
    private final Integer burstTime;
    private final Integer waitingTime;
    private final Integer turnaroundTime;
    private final Double normalizedTurnaroundTime;

    public static ProcessResponseDto from(Process process) {
        return ProcessResponseDto.builder()
                .name(process.getName())
                .arrivalTime(process.getArrivalTime().getSecond())
                .burstTime(process.getBurstTime().getSecond())
                .waitingTime(process.getWaitingTime().getSecond())
                .turnaroundTime(process.getTurnaroundTime().getSecond())
                .normalizedTurnaroundTime(process.getNormalizedTurnaroundTime().getSecond())
                .build();
    }
}
