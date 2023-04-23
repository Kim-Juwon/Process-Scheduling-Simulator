package com.pssimulator.domain.workload;

import lombok.Getter;

@Getter
public class Workload {
    private static final Integer ZERO = 0;

    private Integer workload;

    private Workload(Integer workload) {
        this.workload = workload;
    }

    public static Workload from(Integer workload) {
        return new Workload(workload);
    }

    public boolean isZero() {
        return workload.equals(ZERO);
    }

    public void decreaseBy(Integer workload) {
        this.workload -= workload;

        if (this.workload < 0) {
            this.workload = 0;
        }
    }

    public int compare(Workload workload) {
        return this.workload - workload.getWorkload();
    }
}
