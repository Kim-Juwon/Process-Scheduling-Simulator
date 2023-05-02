package com.pssimulator.domain.grant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Grant {
    private boolean granted;

    public static Grant createFalse() {
        return new Grant(false);
    }

    public boolean isGranted() {
        return granted;
    }

    public void grant() {
        granted = true;
    }

    public void ungrant() {
        granted = false;
    }
}
