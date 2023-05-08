package com.pssimulator.domain.queue;

import com.pssimulator.domain.process.Processes;

/**
 *  선점 알고리즘 스케줄러에서 사용하는 queue에서 구현해야 하는 interface
 */
public interface Preemptible {
    void addPreemptedProcesses(Processes preemptedProcesses);
}
