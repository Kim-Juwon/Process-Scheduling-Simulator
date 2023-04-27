package com.pssimulator.domain.processor;

import com.pssimulator.dto.request.ProcessorRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class Processor {
    private final String name;
    private final Core core;
    private final PowerConsumption totalPowerConsumption;
    private boolean startupRequired;

    public static Processor from(ProcessorRequestDto dto) {
        return Processor.builder()
                .name(dto.getName())
                .core(Core.from(dto.getCore()))
                .totalPowerConsumption(PowerConsumption.createZero())
                .startupRequired(true)
                .build();
    }

    public Integer getThroughputPerSecond() {
        return core.getThroughputPerSecond();
    }

    public PowerConsumption increasePowerConsumption() {
        Double increasedPowerConsumption = 0.0;

        if (startupRequired) {
            totalPowerConsumption.increaseBy(core.getStartupPower());
            increasedPowerConsumption += core.getStartupPower();
            changeToNoRequiredStartupPower();
        }

        totalPowerConsumption.increaseBy(core.getPowerConsumptionPerSecond());
        increasedPowerConsumption += core.getPowerConsumptionPerSecond();

        return PowerConsumption.from(increasedPowerConsumption);
    }

    public void changeToRequiredStartupPower() {
        startupRequired = true;
    }

    private void changeToNoRequiredStartupPower() {
        startupRequired = false;
    }
}
