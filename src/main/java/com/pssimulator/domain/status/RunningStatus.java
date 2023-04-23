package com.pssimulator.domain.status;

import com.pssimulator.domain.process.Pair;
import com.pssimulator.domain.process.Pairs;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.processor.PowerConsumption;
import com.pssimulator.domain.processor.Processors;
import com.pssimulator.domain.time.IntegerTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RunningStatus {
    private final IntegerTime currentTime;
    private final Pairs pairs;
    private final PowerConsumption totalPowerConsumption;

    public static RunningStatus create() {
        return new RunningStatus(IntegerTime.createZero(), Pairs.createEmpty(), PowerConsumption.createZero());
    }

    public boolean isProcessesExist() {
        return !pairs.isEmpty();
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

    public void removeTerminatedPairs() {
        pairs.removeTerminatedPairs();
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
}

