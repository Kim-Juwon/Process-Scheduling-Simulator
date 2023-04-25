package com.pssimulator.domain.status;

import com.pssimulator.domain.process.Pair;
import com.pssimulator.domain.process.Pairs;
import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.processor.PowerConsumption;
import com.pssimulator.domain.processor.Processors;
import com.pssimulator.domain.queue.ReadyQueue;
import com.pssimulator.domain.time.IntegerTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RunningStatus {
    private final IntegerTime currentTime;
    private final Pairs pairs;
    private final PowerConsumption totalPowerConsumption;

    public static RunningStatus create() {
        return new RunningStatus(IntegerTime.createZero(), Pairs.createEmpty(), PowerConsumption.createZero());
    }

    public IntegerTime getCurrentTime() {
        return currentTime;
    }

    public boolean isTerminatedProcessExist() {
        return pairs.isTerminatedProcessExist();
    }

    public boolean isProcessesEmpty() {
        return pairs.isEmpty();
    }

    public void addPair(Pair pair) {
        pairs.add(pair);
    }

    public Processes getTerminatedProcesses() {
        return pairs.getTerminatedProcesses();
    }

    public Processors getTerminatedProcessors() {
        return pairs.getTerminatedProcessors();
    }

    public Processors getProcessors() {
        return pairs.getProcessors();
    }

    public void removeTerminatedPairs() {
        pairs.removeTerminatedPairs();
    }

    public void removeTimeQuantumExpiredPairs(IntegerTime timeQuantum) {
        pairs.removeTimeQuantumExpiredPairs(timeQuantum);
    }

    public void updateWorkloadAndBurstTimeOfProcesses() {
        pairs.updateWorkloadAndBurstTimeOfProcesses();
    }

    public void updatePowerConsumption() {
        PowerConsumption increasedPowerConsumption = updatePowerConsumptionOfProcessors();
        updateTotalPowerConsumptionFrom(increasedPowerConsumption);
    }

    private PowerConsumption updatePowerConsumptionOfProcessors() {
        return pairs.updatePowerConsumptionOfProcessors();
    }

    private void updateTotalPowerConsumptionFrom(PowerConsumption increasedPowerConsumption) {
        totalPowerConsumption.increaseBy(increasedPowerConsumption);
    }

    public void increaseCurrentTime() {
        currentTime.increase();
    }

    public boolean isTimeQuantumExpiredProcessExist(IntegerTime timeQuantum) {
        return pairs.isTimeQuantumExpiredProcessExist(timeQuantum);
    }

    public Processes getTimeQuantumExpiredProcesses(IntegerTime timeQuantum) {
        return pairs.getTimeQuantumExpiredProcesses(timeQuantum);
    }

    public Processors getProcessorsAboutTimeQuantumExpiredProcesses(IntegerTime timeQuantum) {
        return pairs.getProcessorsAboutTimeQuantumExpiredProcesses(timeQuantum);
    }

    public boolean isLessRemainingWorkloadProcessExistIn(ReadyQueue readyQueue) {
        return pairs.isLessRemainingWorkloadProcessExistFrom(readyQueue);
    }

    public Processes getBiggerWorkloadProcessesComparedWith(ReadyQueue readyQueue) {
        return pairs.getBiggerWorkloadProcessesComparedWith(readyQueue);
    }
}

