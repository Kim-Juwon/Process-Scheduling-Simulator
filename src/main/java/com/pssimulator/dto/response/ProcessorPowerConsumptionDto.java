package com.pssimulator.dto.response;

import com.pssimulator.domain.processor.Processor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProcessorPowerConsumptionDto {
    private final String processorName;
    private final Double totalPowerConsumption;

    public static ProcessorPowerConsumptionDto from(Processor processor) {
        return new ProcessorPowerConsumptionDto(processor.getName(), processor.getTotalPowerConsumption().getPowerConsumption());
    }

    public int compareByProcessorNameAscending(ProcessorPowerConsumptionDto processorPowerConsumptionDto) {
        return processorName.compareTo(processorPowerConsumptionDto.getProcessorName());
    }
}
