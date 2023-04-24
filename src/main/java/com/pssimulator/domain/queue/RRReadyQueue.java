package com.pssimulator.domain.queue;

import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.time.IntegerTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Getter
@RequiredArgsConstructor
public class RRReadyQueue extends ReadyQueue {
    private final Queue<Process> readyQueue;

    public static RRReadyQueue createEmpty() {
        return new RRReadyQueue(new LinkedList<>());
    }

    @Override
    public boolean isEmpty() {
        return readyQueue.isEmpty();
    }

    @Override
    public void addArrivedProcessesFrom(Processes processes, IntegerTime time) {
        List<Process> arrivedProcesses = processes.getArrivedProcessesAt(time);
        readyQueue.addAll(arrivedProcesses);
    }

    @Override
    public Process getNextProcess() {
        return readyQueue.poll();
    }

    @Override
    public void increaseWaitingTimeOfProcesses() {
        List<Process> processes = new ArrayList<>();

        while (!readyQueue.isEmpty()) {
            Process process = readyQueue.poll();
            process.increaseWaitingTime();
            processes.add(process);
        }

        readyQueue.addAll(processes);
    }

    public void addPreemptedProcesses(Processes preemptedProcesses) {
        readyQueue.addAll(preemptedProcesses.getProcesses());
    }
}
