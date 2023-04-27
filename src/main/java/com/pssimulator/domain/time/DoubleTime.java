package com.pssimulator.domain.time;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DoubleTime {
    private static final Double EMPTY = null;
    private Double second;

    public static DoubleTime createEmpty() {
        return new DoubleTime(EMPTY);
    }

    public static DoubleTime from(Double second) {
        return new DoubleTime(second);
    }

    public void changeTo(DoubleTime time) {
        this.second = time.second;
    }
}
