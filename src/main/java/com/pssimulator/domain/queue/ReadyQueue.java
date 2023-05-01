package com.pssimulator.domain.queue;

import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.time.IntegerTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Queue;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ReadyQueue {
    protected final Queue<Process> readyQueue;

    public abstract boolean isEmpty();

    public abstract void addArrivedProcessesFrom(Processes processes, IntegerTime time);

    public abstract Process getNextProcess();

    public abstract void increaseWaitingTimeOfProcesses();

    public abstract List<Process> peekCurrentProcesses();
}
