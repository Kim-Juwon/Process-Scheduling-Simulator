package com.pssimulator.dto.response;

import com.pssimulator.domain.process.Pair;
import com.pssimulator.domain.process.Pairs;
import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.processor.PowerConsumption;
import com.pssimulator.domain.processor.Processor;
import com.pssimulator.domain.processor.Processors;
import com.pssimulator.domain.queue.FCFSReadyQueue;
import com.pssimulator.domain.queue.HRRNReadyQueue;
import com.pssimulator.domain.queue.RRReadyQueue;
import com.pssimulator.domain.queue.ReadyQueue;
import com.pssimulator.domain.queue.SPNReadyQueue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeStatusResponseDto {
    private Integer from;
    private Integer to;
    private final List<PairResponseDto> pairs;
    private final List<ProcessorPowerConsumptionDto> processorPowerConsumptions;
    private Double totalPowerConsumption;
    private final List<String> readyQueue;
    private final List<ProcessResponseDto> terminatedProcesses;

    public static TimeStatusResponseDto from(Integer to) {
        return new TimeStatusResponseDto(
                to - 1,
                to,
                new ArrayList<>(),
                new ArrayList<>(),
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public void addPairs(Pairs runningPairs) {
        List<Pair> pairs = runningPairs.getPairs();
        pairs.sort(Pair::compareByProcessorName);

        pairs.forEach(pair -> {
            this.pairs.add(PairResponseDto.from(pair));
        });
    }

    public void addProcessorPowerConsumptions(Processors allProcessors) {
        List<Processor> processors = allProcessors.getProcessors();
        processors.sort(Processor::compareTo);

        processors.forEach(processor -> {
            processorPowerConsumptions.add(ProcessorPowerConsumptionDto.from(processor));
        });
    }

    public void addTotalPowerConsumption(PowerConsumption totalPowerConsumption) {
        this.totalPowerConsumption = totalPowerConsumption.getPowerConsumption();
    }

    public void addReadyQueue(ReadyQueue readyQueue) {
        List<Process> processes = readyQueue.peekCurrentProcesses();
        processes.forEach(process -> {
            this.readyQueue.add(process.getName());
        });
    }

    public void addTerminatedProcessesFrom(Processes terminatedProcesses) {
        List<Process> processes = terminatedProcesses.getProcesses();

        processes.forEach(process -> {
            this.terminatedProcesses.add(ProcessResponseDto.from(process));
        });
    }
}
