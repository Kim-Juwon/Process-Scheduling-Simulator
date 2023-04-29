package com.pssimulator.domain.queue;

import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.time.IntegerTime;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class HRRNReadyQueue extends ReadyQueue {
    private HRRNReadyQueue(Queue<Process> readyQueue) {
        super(readyQueue);
    }

    public static HRRNReadyQueue createEmpty() {
        return new HRRNReadyQueue(new PriorityQueue<>(Process::compareByHRRN));
    }

    @Override
    public boolean isEmpty() {
        return readyQueue.isEmpty();
    }

    @Override
    public void addArrivedProcessesFrom(Processes processes, IntegerTime currentTime) {
        updateBasedOnResponseRatio();

        List<Process> arrivedProcesses = processes.getArrivedProcessesAt(currentTime);
        calculateResponseRatioFrom(arrivedProcesses);
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
        List<Process> processes = new ArrayList<>();

        while (!readyQueue.isEmpty()) {
            processes.add(readyQueue.poll());
        }

        readyQueue.addAll(processes);

        return processes;
    }

    private void updateBasedOnResponseRatio() {
        List<Process> processes = new ArrayList<>();

        while (!readyQueue.isEmpty()) {
            processes.add(readyQueue.poll());
        }

        processes.forEach(Process::calculateResponseRatio);
        readyQueue.addAll(processes);
    }

    private void calculateResponseRatioFrom(List<Process> arrivedProcesses) {
        arrivedProcesses.forEach(Process::calculateResponseRatio);
    }
}
