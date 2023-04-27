package com.pssimulator.domain.ratio;

import com.pssimulator.domain.time.IntegerTime;
import com.pssimulator.domain.workload.Workload;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseRatio {
    private static final Double EMPTY = null;

    private Double responseRatio;

    public static ResponseRatio createEmpty() {
        return new ResponseRatio(EMPTY);
    }

    public void changeFrom(IntegerTime waitingTime, Workload workload) {
        this.responseRatio = (double) (waitingTime.getSecond() + workload.getWorkload()) / workload.getWorkload();
    }

    public int compareByDescending(ResponseRatio responseRatio) {
        if (this.responseRatio > responseRatio.getResponseRatio()) {
            return -1;
        } else if (this.responseRatio.equals(responseRatio.getResponseRatio())) {
            return 0;
        } else {
            return 1;
        }
    }
}
