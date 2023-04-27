package com.pssimulator.domain.time;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegerTime {
    private static final Integer ZERO = 0;
    private static final Integer EMPTY = null;

    private Integer second;

    public static IntegerTime createZero() {
        return new IntegerTime(ZERO);
    }

    public static IntegerTime createEmpty() {
        return new IntegerTime(EMPTY);
    }

    public static IntegerTime from(Integer second) {
        return new IntegerTime(second);
    }

    public void increase() {
        ++second;
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

    public boolean isSameOrHigher(IntegerTime time) {
        return second >= time.getSecond();
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
