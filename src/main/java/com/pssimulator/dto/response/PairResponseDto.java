package com.pssimulator.dto.response;

import com.pssimulator.domain.process.Pair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PairResponseDto {
    private final String processName;
    private final String processorName;

    public static PairResponseDto from(Pair pair) {
        return new PairResponseDto(pair.getProcess().getName(), pair.getProcessor().getName());
    }
}
