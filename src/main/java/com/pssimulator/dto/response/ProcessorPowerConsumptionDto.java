package com.pssimulator.dto.response;

import com.pssimulator.domain.processor.Processor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
public class ProcessorPowerConsumptionDto {
    @Getter
    private final String processorName;
    private final Double totalPowerConsumption;

    public static ProcessorPowerConsumptionDto from(Processor processor) {
        return new ProcessorPowerConsumptionDto(processor.getName(), processor.getTotalPowerConsumption().getPowerConsumption());
    }

    public Double getTotalPowerConsumption() {
        return new BigDecimal(totalPowerConsumption)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public int compareByProcessorNameAscending(ProcessorPowerConsumptionDto processorPowerConsumptionDto) {
        return processorName.compareTo(processorPowerConsumptionDto.getProcessorName());
    }
}
