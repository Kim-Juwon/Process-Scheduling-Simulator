package com.pssimulator.domain.time;

import lombok.Getter;

@Getter
public class DoubleTime {
    private Double second;

    private DoubleTime(Double second) {
        this.second = second;
    }

    public static DoubleTime createEmpty() {
        return new DoubleTime(null);
    }

    public static DoubleTime from(Double second) {
        return new DoubleTime(second);
    }

    public void changeTo(DoubleTime time) {
        this.second = time.second;
    }
}
