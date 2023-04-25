package com.pssimulator.domain.process;

import com.pssimulator.domain.processor.PowerConsumption;
import com.pssimulator.domain.processor.Processor;
import com.pssimulator.domain.processor.Processors;
import com.pssimulator.domain.queue.ReadyQueue;
import com.pssimulator.domain.time.IntegerTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class Pairs {
    private final List<Pair> pairs;

    public static Pairs createEmpty() {
        return new Pairs(new LinkedList<>());
    }

    public boolean isEmpty() {
        return pairs.isEmpty();
    }

    public void add(Pair pair) {
        pairs.add(pair);
    }

    public Processes getProcesses() {
        if (pairs.isEmpty()) {
            return Processes.createEmpty();
        }

        List<Process> processList = pairs.stream()
                .map(Pair::getProcess)
                .collect(Collectors.toList());

        return Processes.fromProcesses(processList);
    }

    public boolean isTerminatedProcessExist() {
        return pairs.stream()
                .map(Pair::getProcess)
                .anyMatch(Process::isTerminated);
    }

    public Processes getTerminatedProcesses() {
        List<Process> terminatedProcesses = pairs.stream()
                .map(Pair::getProcess)
                .filter(Process::isTerminated)
                .collect(Collectors.toList());

        return Processes.fromProcesses(terminatedProcesses);
    }

    public Processors getTerminatedProcessors() {
        List<Processor> terminatedProcessors = new ArrayList<>();

        pairs.forEach(pair -> {
            Process process = pair.getProcess();
            if (process.isTerminated()) {
                terminatedProcessors.add(pair.getProcessor());
            }
        });

        return Processors.fromProcessors(terminatedProcessors);
    }

    public void removeTerminatedPairs() {
        for (int i = 0; i < pairs.size(); i++) {
            Pair pair = pairs.get(i);
            Process process = pair.getProcess();

            if (process.isTerminated()) {
                pairs.remove(i);
                i--;
            }
        }
    }

    public void removeTimeQuantumExpiredPairs(IntegerTime timeQuantum) {
        for (int i = 0; i < pairs.size(); i++) {
            Pair pair = pairs.get(i);

            if (pair.isProcessTimeQuantumExpired(timeQuantum)) {
                pairs.remove(i);
                i--;
            }
        }
    }

    public void updateWorkloadAndBurstTimeOfProcesses() {
        pairs.forEach(Pair::updateWorkloadAndBurstTimeOfProcess);
    }

    public PowerConsumption updatePowerConsumptionOfProcessors() {
        PowerConsumption increasedPowerConsumption = PowerConsumption.createZero();

        pairs.forEach(pair -> {
            increasedPowerConsumption.increaseBy(pair.updatePowerConsumptionOfProcessor());
        });

        return increasedPowerConsumption;
    }

    public Processors getProcessors() {
        List<Processor> processors = pairs.stream()
                .map(Pair::getProcessor)
                .collect(Collectors.toList());

        return Processors.fromProcessors(processors);
    }

    public boolean isTimeQuantumExpiredProcessExist(IntegerTime timeQuantum) {
        return pairs.stream()
                .anyMatch(pair -> pair.isProcessTimeQuantumExpired(timeQuantum));
    }

    public Processes getTimeQuantumExpiredProcesses(IntegerTime timeQuantum) {
        List<Process> timeQuantumExpiredProcesses = pairs.stream()
                .filter(pair -> pair.isProcessTimeQuantumExpired(timeQuantum))
                .map(Pair::getProcess)
                .collect(Collectors.toList());

        return Processes.fromProcesses(timeQuantumExpiredProcesses);
    }

    public Processors getProcessorsAboutTimeQuantumExpiredProcesses(IntegerTime timeQuantum) {
        List<Processor> processors = pairs.stream()
                .filter(pair -> pair.isProcessTimeQuantumExpired(timeQuantum))
                .map(Pair::getProcessor)
                .collect(Collectors.toList());

        return Processors.fromProcessors(processors);
    }

    public boolean isLessRemainingWorkloadProcessExistFrom(ReadyQueue readyQueue) {
        return readyQueue.peekCurrentProcesses().stream()
                .anyMatch(processInReadyQueue -> pairs.stream()
                        .anyMatch(pair -> pair.isProcessRemainingWorkloadBiggerThan(processInReadyQueue))
                );
    }

    public Processes getBiggerWorkloadProcessesComparedWith(ReadyQueue readyQueue) {
        /*readyQueue.peekCurrentProcesses().forEach(processInReadyQueue -> {
            pairs.forEach(pair -> {
                if (pair.isProcessRemainingWorkloadBiggerThan())
            });
        });*/
        return null;
    }
}
