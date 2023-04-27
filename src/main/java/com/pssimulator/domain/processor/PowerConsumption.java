package com.pssimulator.domain.processor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PowerConsumption {
    private static final Double ZERO = 0.0;

    private Double powerConsumption;

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
