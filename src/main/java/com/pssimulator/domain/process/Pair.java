package com.pssimulator.domain.process;

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

    public PowerConsumption updatePowerConsumptionOfProcessor() {
        return processor.increasePowerConsumption();
    }

    public boolean isProcessTimeQuantumExpired(IntegerTime timeQuantum) {
        return process.isTimeQuantumExpired(timeQuantum);
    }

    public boolean isProcessRemainingWorkloadBiggerThan(Process process) {
        return this.process.isRemainingWorkloadBiggerThan(process);
    }

    public int compareByProcessorName(Pair pair) {
        return processor.compareTo(pair.getProcessor());
    }

    public int compareByProcessRemainingWorkloadDescending(Pair pair) {
        return process.compareByRemainingWorkloadDescending(pair.getProcess());
    }
}
