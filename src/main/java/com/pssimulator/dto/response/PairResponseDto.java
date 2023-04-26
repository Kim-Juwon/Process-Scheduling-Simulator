package com.pssimulator.dto.response;

import com.pssimulator.domain.pair.Pair;
import com.pssimulator.domain.processor.Processor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PairResponseDto {
    private final String processorName;
    private final String processName;

    public static PairResponseDto from(Pair pair) {
        return new PairResponseDto(pair.getProcessor().getName(), pair.getProcess().getName());
    }

    public static PairResponseDto from(Processor processor) {
        return new PairResponseDto(processor.getName(), null);
    }

    public int compareByProcessorNameAscending(PairResponseDto pairResponseDto) {
        return processorName.compareTo(pairResponseDto.getProcessorName());
    }
}
