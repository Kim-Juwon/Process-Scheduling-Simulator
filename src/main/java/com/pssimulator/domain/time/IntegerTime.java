package com.pssimulator.domain.time;

import lombok.Getter;

public class IntegerTime {
    private static final Integer ZERO = 0;

    @Getter
    private Integer second;

    private IntegerTime(Integer second) {
        this.second = second;
    }

    public static IntegerTime createZero() {
        return new IntegerTime(ZERO);
    }

    public static IntegerTime createEmpty() {
        return new IntegerTime(null);
    }

    public static IntegerTime from(Integer second) {
        return new IntegerTime(second);
    }

    public boolean isZero() {
        return second.equals(ZERO);
    }

    public void increase() {
        ++second;
    }

    public void decrease() {
        --second;
    }

    public void changeTo(IntegerTime time) {
        this.second = time.getSecond();
    }

    public void changeToZero() {
        this.second = ZERO;
    }

    public IntegerTime add(IntegerTime time) {
        return IntegerTime.from(second + time.getSecond());
    }

    public DoubleTime divide(IntegerTime time) {
        return DoubleTime.from((double) second / time.getSecond());
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IntegerTime)) {
            return false;
        }

        return second.equals(((IntegerTime) obj).getSecond());
    }
}
