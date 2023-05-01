package com.pssimulator.domain.workload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "from")
public class Workload {
    private static final Integer ZERO = 0;

    private Integer workload;

    public boolean isZero() {
        return workload.equals(ZERO);
    }

    public void decreaseBy(Integer workload) {
        this.workload -= workload;

        if (this.workload < 0) {
            this.workload = 0;
        }
    }

    public boolean isBiggerThan(Workload workload) {
        return this.workload > workload.getWorkload();
    }

    public boolean isBiggerThan(Double workloadAverage) {
        return workload > workloadAverage;
    }

    public int compareByAscending(Workload workload) {
        return this.workload - workload.getWorkload();
    }

    public int compareByDescending(Workload workload) {
        return workload.getWorkload() - this.workload;
    }
}
