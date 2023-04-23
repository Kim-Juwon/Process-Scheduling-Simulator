package com.pssimulator.domain.processor;

import com.pssimulator.dto.request.CoreDto;
import lombok.Getter;

@Getter
public enum Core {
    E(1, 1.0, 0.1),
    P(2, 3.0, 0.5);

    Core(int throughputPerSecond, double powerConsumptionPerSecond, double startupPower) {
        this.throughputPerSecond = throughputPerSecond;
        this.powerConsumptionPerSecond = powerConsumptionPerSecond;
        this.startupPower = startupPower;
    }

    private final int throughputPerSecond;
    private final double powerConsumptionPerSecond;
    private final double startupPower;

    public static Core from(CoreDto dto) {
        for (Core core : values()) {
            if (core.isSame(dto)) {
                return core;
            }
        }
        throw new RuntimeException();
    }

    private boolean isSame(CoreDto dto) {
        return name().equals(dto.name());
    }
}
