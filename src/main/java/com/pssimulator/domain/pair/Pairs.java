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

    // 삭제가 빈번하게 일어나므로 객체 생성시 linked list로 구성한다.
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

        // remove terminated pairs
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

        // remove time quantum expired pairs
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
        return pairs.stream()
                .anyMatch(pair ->
                        readyQueue.peekCurrentProcesses().stream()
                                .anyMatch(pair::isProcessRemainingWorkloadBiggerThan)
                );
    }

    public Pairs getBiggerRemainingWorkloadPairsComparedWith(ReadyQueue readyQueue) {
        // 실제 pair 리스트의 순서를 유지하기 위해, 복사된 pair 리스트 생성
        List<Pair> tempPairs = new LinkedList<>(pairs);
        tempPairs.sort(Pair::compareByProcessRemainingWorkloadAscending);

        List<Process> readyProcesses = new LinkedList<>(readyQueue.peekCurrentProcesses());
        readyProcesses.sort(Process::compareByRemainingWorkloadAscending);

        List<Pair> biggerRemainingWorkloadPairs = new ArrayList<>();

        for (int i = 0; i < tempPairs.size(); i++) {
            if (readyProcesses.isEmpty()) {
                break;
            }

            Pair pair = tempPairs.get(i);
            Process shortestRemainingWorkloadReadyProcess = readyProcesses.get(0);

            if (pair.isProcessRemainingWorkloadBiggerThan(shortestRemainingWorkloadReadyProcess)) {
                biggerRemainingWorkloadPairs.add(pair);

                tempPairs.remove(i); i--;
                pairs.remove(pair);
                readyProcesses.remove(0);
            }
        }

        return Pairs.from(biggerRemainingWorkloadPairs);
    }

    public Pairs getMNPreemptionConditionPairs(IntegerTime timeQuantum) {
        List<Pair> preemptedPairs = new ArrayList<>();

        for (int i = 0; i < pairs.size(); i++) {
            Pair pair = pairs.get(i);

            if (pair.isProcessTimeQuantumExpired(timeQuantum)) {
                if (pair.isProcessMalneon()) {
                    // 한번 더 시간이 부여되었던 말년 프로세스라면 preempt (starvation 방지)
                    if (pair.isProcessAdditionalTimeGranted()) {
                        preemptedPairs.add(pair);
                        pairs.remove(i);
                        i--;
                    }
                    // 추가 시간이 부여되지 않았던 말년 프로세스라면, running burst time 초기화하고 추가 시간 부여
                    else {
                        Process process = pair.getProcess();
                        process.initializeRunningBurstTime();
                        process.grantAdditionalTime();
                    }
                } else {
                    preemptedPairs.add(pair);
                    pairs.remove(i);
                    i--;
                }
            }
        }

        return Pairs.from(preemptedPairs);
    }
}
