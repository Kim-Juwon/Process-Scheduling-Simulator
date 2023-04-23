package com.pssimulator.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pssimulator.domain.process.Pairs;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.processor.PowerConsumption;
import com.pssimulator.domain.queue.ReadyQueue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Response {
    private final List<TimeStatusResponseDto> statuses;

    @JsonIgnore
    private TimeStatusResponseDto currentTimeStatus;
    @JsonIgnore
    private Integer currentTime;

    public static Response create() {
        return new Response(new ArrayList<>(), TimeStatusResponseDto.from(1), 1);
    }

    public void applyCurrentTimeStatus() {
        statuses.add(currentTimeStatus);
        moveToNextTime();
    }

    private void moveToNextTime() {
        increaseCurrentTime();
        initializeCurrentTimeStatus();
    }

    private void increaseCurrentTime() {
        ++currentTime;
    }

    private void initializeCurrentTimeStatus() {
        currentTimeStatus = TimeStatusResponseDto.from(currentTime);
    }

    public void addPairs(Pairs pairs) {
        currentTimeStatus.addPairs(pairs);
    }

    public void addTotalPowerConsumption(PowerConsumption totalPowerConsumption) {
        currentTimeStatus.addTotalPowerConsumption(totalPowerConsumption);
    }

    public void addReadyQueue(ReadyQueue readyQueue) {
        currentTimeStatus.addReadyQueue(readyQueue);
    }

    public void addTerminatedProcessesFrom(Processes terminatedRunningProcesses) {
        currentTimeStatus.addTerminatedProcessesFrom(terminatedRunningProcesses);
    }
}
