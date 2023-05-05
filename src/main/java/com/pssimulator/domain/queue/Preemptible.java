package com.pssimulator.domain.queue;

import com.pssimulator.domain.process.Processes;

public interface Preemptible {
    void addPreemptedProcesses(Processes preemptedProcesses);
}
