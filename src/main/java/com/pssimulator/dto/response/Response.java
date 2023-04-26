package com.pssimulator.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pssimulator.domain.process.Pairs;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.processor.PowerConsumption;
import com.pssimulator.domain.processor.Processors;
import com.pssimulator.domain.queue.ReadyQueue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Response {
    private static final Integer START_TIME = 1;

    private final List<TimeStatusResponseDto> statuses;

    @JsonIgnore
    private TimeStatusResponseDto currentTimeStatus;
    @JsonIgnore
    private Integer currentTime;

    public static Response create() {
        return new Response(new ArrayList<>(), TimeStatusResponseDto.from(START_TIME), START_TIME);
    }

    public void apply() {
        addCurrentTimeStatusFrom(currentTimeStatus);
        moveToNextTime();
        initializeCurrentTimeStatus();
    }

    private void moveToNextTime() {
        ++currentTime;
    }

    private void initializeCurrentTimeStatus() {
        currentTimeStatus = TimeStatusResponseDto.from(currentTime);
    }

    private void addCurrentTimeStatusFrom(TimeStatusResponseDto timeStatusResponseDto) {
        statuses.add(timeStatusResponseDto);
    }

    public void addRunningStateFrom(Pairs runningPairs, Processors restingProcessors) {
        currentTimeStatus.addRunningStateFrom(runningPairs, restingProcessors);
    }

    public void addProcessorPowerConsumptionsFrom(Processors processors) {
        currentTimeStatus.addProcessorPowerConsumptions(processors);
    }

    public void addTotalPowerConsumptionFrom(PowerConsumption totalPowerConsumption) {
        currentTimeStatus.addTotalPowerConsumption(totalPowerConsumption);
    }

    public void addReadyQueueFrom(ReadyQueue readyQueue) {
        currentTimeStatus.addReadyQueue(readyQueue);
    }

    public void addTerminatedProcessesFrom(Processes terminatedRunningProcesses) {
        currentTimeStatus.addTerminatedProcessesFrom(terminatedRunningProcesses);
    }
}
