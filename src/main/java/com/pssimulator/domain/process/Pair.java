package com.pssimulator.domain.process;

import com.pssimulator.domain.processor.PowerConsumption;
import com.pssimulator.domain.processor.Processor;
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
}
