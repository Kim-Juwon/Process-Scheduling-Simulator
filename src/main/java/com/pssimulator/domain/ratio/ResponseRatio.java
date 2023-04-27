package com.pssimulator.domain.ratio;

import com.pssimulator.domain.time.IntegerTime;
import com.pssimulator.domain.workload.Workload;
import lombok.Getter;

@Getter
public class ResponseRatio {
    private Double responseRatio;

    private ResponseRatio(Double responseRatio) {
        this.responseRatio = responseRatio;
    }

    public static ResponseRatio createEmpty() {
        return new ResponseRatio(null);
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
