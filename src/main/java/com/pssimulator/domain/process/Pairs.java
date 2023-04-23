package com.pssimulator.domain.process;

import com.pssimulator.domain.processor.PowerConsumption;
import com.pssimulator.domain.processor.Processor;
import com.pssimulator.domain.processor.Processors;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Pair> getPairs() {
        return Collections.unmodifiableList(pairs);
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
}
