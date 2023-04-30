package com.pssimulator.domain.pair;

import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
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

    public static Pairs from(List<Pair> pairs) {
        return new Pairs(new LinkedList<>(pairs));
    }

    public boolean isEmpty() {
        return pairs.isEmpty();
    }

    public void add(Pair pair) {
        pairs.add(pair);
    }

    public Processes getProcesses() {
        List<Process> processes = pairs.stream()
                .map(Pair::getProcess)
                .collect(Collectors.toList());

        return Processes.fromProcesses(processes);
    }

    public Processors getProcessors() {
        List<Processor> processors = pairs.stream()
                .map(Pair::getProcessor)
                .collect(Collectors.toList());

        return Processors.fromProcessors(processors);
    }

    public boolean isTerminatedProcessExist() {
        return pairs.stream()
                .map(Pair::getProcess)
                .anyMatch(Process::isTerminated);
    }

    public Pairs getTerminatedPairs() {
        List<Pair> terminatedPairs = new ArrayList<>();

        for (int i = 0; i < pairs.size(); i++) {
            Pair pair = pairs.get(i);
            if (pair.isProcessTerminated()) {
                terminatedPairs.add(pair);
                pairs.remove(i);
                i--;
            }
        }

        return Pairs.from(terminatedPairs);
    }

    public Processes getTerminatedProcesses() {
        List<Process> terminatedProcesses = pairs.stream()
                .map(Pair::getProcess)
                .filter(Process::isTerminated)
                .collect(Collectors.toList());

        return Processes.fromProcesses(terminatedProcesses);
    }

    public Processors getTerminatedProcessors() {
        List<Processor> terminatedProcessors = pairs.stream()
                .filter(Pair::isProcessTerminated)
                .map(Pair::getProcessor)
                .collect(Collectors.toList());

        return Processors.fromProcessors(terminatedProcessors);
    }

    public void removeTerminatedPairs() {
        for (int i = 0; i < pairs.size(); i++) {
            Pair pair = pairs.get(i);
            if (pair.isProcessTerminated()) {
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

    public boolean isTimeQuantumExpiredProcessExist(IntegerTime timeQuantum) {
        return pairs.stream()
                .anyMatch(pair -> pair.isProcessTimeQuantumExpired(timeQuantum));
    }

    public Pairs getTimeQuantumExpiredPairs(IntegerTime timeQuantum) {
        List<Pair> timeQuantumExpiredPairs = new ArrayList<>();

        for (int i = 0; i < pairs.size(); i++) {
            Pair pair = pairs.get(i);
            if (pair.isProcessTimeQuantumExpired(timeQuantum)) {
                timeQuantumExpiredPairs.add(pair);
                pairs.remove(i);
                i--;
            }
        }

        return Pairs.from(timeQuantumExpiredPairs);
    }

    public boolean isLessRemainingWorkloadProcessExistFrom(ReadyQueue readyQueue) {
        List<Process> processesInReadyQueue = readyQueue.peekCurrentProcesses();

        processesInReadyQueue.sort(Process::compareByRemainingWorkloadDescending);
        pairs.sort(Pair::compareByProcessRemainingWorkloadDescending);

        return pairs.stream()
                .anyMatch(pair -> readyQueue.peekCurrentProcesses().stream()
                        .anyMatch(pair::isProcessRemainingWorkloadBiggerThan)
                );
    }

    public Pairs getBiggerRemainingWorkloadPairsComparedWith(ReadyQueue readyQueue) {
        List<Process> processesInReadyQueue = readyQueue.peekCurrentProcesses();

        processesInReadyQueue.sort(Process::compareByRemainingWorkloadDescending);
        pairs.sort(Pair::compareByProcessRemainingWorkloadDescending);

        List<Pair> biggerRemainingWorkloadPairs = new ArrayList<>();
        for (int i = 0; i < pairs.size(); i++) {
            Pair runningPair = pairs.get(i);
            for (int j = 0; j < processesInReadyQueue.size(); j++) {
                Process processInReadyQueue = processesInReadyQueue.get(j);
                if (runningPair.isProcessRemainingWorkloadBiggerThan(processInReadyQueue)) {
                    biggerRemainingWorkloadPairs.add(runningPair);
                    pairs.remove(i);
                    processesInReadyQueue.remove(j);
                    i--;
                    break;
                }
            }
        }

        return Pairs.from(biggerRemainingWorkloadPairs);
    }
}
