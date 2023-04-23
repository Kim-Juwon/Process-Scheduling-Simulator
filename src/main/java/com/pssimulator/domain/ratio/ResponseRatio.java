package com.pssimulator.domain.ratio;

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
}
