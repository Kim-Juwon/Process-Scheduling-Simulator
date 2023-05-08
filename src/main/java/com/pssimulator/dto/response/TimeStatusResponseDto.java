package com.pssimulator.dto.response;

import com.pssimulator.domain.pair.Pairs;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.processor.PowerConsumption;
import com.pssimulator.domain.processor.Processors;
import com.pssimulator.domain.queue.ReadyQueue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeStatusResponseDto {
    @Getter
    private final Integer from;

    @Getter
    private final Integer to;

    @Getter
    private final List<PairResponseDto> pairs;

    @Getter
    private final List<ProcessorPowerConsumptionDto> processorPowerConsumptions;

    private Double totalPowerConsumption;

    @Getter
    private final List<String> readyQueue;

    @Getter
    private final List<ProcessResponseDto> terminatedProcesses;

    public static TimeStatusResponseDto from(Integer to) {
        return new TimeStatusResponseDto(to - 1, to, new ArrayList<>(), new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>());
    }

    public Double getTotalPowerConsumption() {
        return new BigDecimal(totalPowerConsumption)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public void addRunningStateFrom(Pairs runningPairs, Processors restingProcessors) {
        runningPairs.getPairs().forEach(runningPair -> {
            this.pairs.add(PairResponseDto.from(runningPair));
        });
        restingProcessors.getProcessors().forEach(restingProcessor -> {
            this.pairs.add(PairResponseDto.from(restingProcessor));
        });
        this.pairs.sort(PairResponseDto::compareByProcessorNameAscending);
    }

    public void addProcessorPowerConsumptions(Processors allProcessors) {
        allProcessors.getProcessors().forEach(processor -> {
            processorPowerConsumptions.add(ProcessorPowerConsumptionDto.from(processor));
        });
        processorPowerConsumptions.sort(ProcessorPowerConsumptionDto::compareByProcessorNameAscending);
    }

    public void addTotalPowerConsumption(PowerConsumption totalPowerConsumption) {
        this.totalPowerConsumption = totalPowerConsumption.getPowerConsumption();
    }

    public void addReadyQueue(ReadyQueue readyQueue) {
        readyQueue.peekCurrentProcesses().forEach(process -> {
            this.readyQueue.add(process.getName());
        });
    }

    public void addTerminatedProcessesFrom(Processes terminatedProcesses) {
        terminatedProcesses.getProcesses().forEach(process -> {
            this.terminatedProcesses.add(ProcessResponseDto.from(process));
        });
        this.terminatedProcesses.sort(ProcessResponseDto::compareByNameAscending);
    }
}
