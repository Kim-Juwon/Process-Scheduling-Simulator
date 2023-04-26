package com.pssimulator.domain.pair;

import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.processor.PowerConsumption;
import com.pssimulator.domain.processor.Processor;
import com.pssimulator.domain.time.IntegerTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class Pair {
    private final Process process;
    private final Processor processor;

    public void updateWorkloadAndBurstTimeOfProcess() {
        process.updateWorkloadAndBurstTimeFrom(processor);
    }

    /**
     *
     * @return increased power consumption object
     */
    public PowerConsumption updatePowerConsumptionOfProcessor() {
        return processor.increasePowerConsumption();
    }

    public boolean isProcessTerminated() {
        return process.isTerminated();
    }

    public boolean isProcessTimeQuantumExpired(IntegerTime timeQuantum) {
        return process.isTimeQuantumExpired(timeQuantum);
    }

    public boolean isProcessRemainingWorkloadBiggerThan(Process process) {
        return this.process.isRemainingWorkloadBiggerThan(process);
    }

    public int compareByProcessRemainingWorkloadDescending(Pair pair) {
        return process.compareByRemainingWorkloadDescending(pair.getProcess());
    }
}
