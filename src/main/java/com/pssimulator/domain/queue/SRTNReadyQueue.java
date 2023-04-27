package com.pssimulator.domain.queue;

import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.time.IntegerTime;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class SRTNReadyQueue extends ReadyQueue {
    private SRTNReadyQueue(Queue<Process> readyQueue) {
        super(readyQueue);
    }

    public static SRTNReadyQueue createEmpty() {
        return new SRTNReadyQueue(new PriorityQueue<>(Process::compareBySRTN));
    }

    @Override
    public boolean isEmpty() {
        return readyQueue.isEmpty();
    }

    @Override
    public void addArrivedProcessesFrom(Processes processes, IntegerTime currentTime) {
        List<Process> arrivedProcesses = processes.getArrivedProcessesAt(currentTime);
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

    @Override
    public List<Process> peekCurrentProcesses() {
        List<Process> processes = new LinkedList<>();

        while (!readyQueue.isEmpty()) {
            processes.add(readyQueue.poll());
        }

        readyQueue.addAll(processes);

        return processes;
    }

    public void addPreemptedProcesses(Processes preemptedProcesses) {
        readyQueue.addAll(preemptedProcesses.getProcesses());
    }
}
