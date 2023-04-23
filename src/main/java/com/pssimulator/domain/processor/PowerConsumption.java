package com.pssimulator.domain.processor;

import lombok.Getter;

@Getter
public class PowerConsumption {
    private static final Double ZERO = 0.0;

    private Double powerConsumption;

    private PowerConsumption(Double powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public static PowerConsumption createZero() {
        return new PowerConsumption(ZERO);
    }

    public static PowerConsumption from(Double powerConsumption) {
        return new PowerConsumption(powerConsumption);
    }

    public void increaseBy(Double powerConsumption) {
        this.powerConsumption += powerConsumption;
    }

    public void increaseBy(PowerConsumption powerConsumption) {
        this.powerConsumption += powerConsumption.getPowerConsumption();
    }
}
