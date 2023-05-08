package com.pssimulator.dto.response;

import com.pssimulator.domain.process.Process;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Builder
public class ProcessResponseDto {
    @Getter
    private final String name;

    @Getter
    private final Integer arrivalTime;

    @Getter
    private final Integer burstTime;

    @Getter
    private final Integer waitingTime;

    @Getter
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

    public Double getNormalizedTurnaroundTime() {
        return new BigDecimal(normalizedTurnaroundTime)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public int compareByNameAscending(ProcessResponseDto processResponseDto) {
        return name.compareTo(processResponseDto.getName());
    }
}
