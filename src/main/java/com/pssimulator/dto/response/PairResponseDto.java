package com.pssimulator.dto.response;

import com.pssimulator.domain.process.Pair;
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
}
