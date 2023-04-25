package com.pssimulator.domain.queue;

import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.time.IntegerTime;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

@RequiredArgsConstructor
public class SPNReadyQueue extends ReadyQueue {
    private final Queue<Process> readyQueue;

    public static SPNReadyQueue createEmpty() {
        return new SPNReadyQueue(new PriorityQueue<>(Process::compareBySPN));
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
        List<Process> processes = new ArrayList<>();

        while (!readyQueue.isEmpty()) {
            processes.add(readyQueue.poll());
        }

        readyQueue.addAll(processes);

        return processes;
    }

    public List<Process> getProcessListAboutPriorityQueue() {
        List<Process> processes = new ArrayList<>();

        while (!readyQueue.isEmpty()) {
            processes.add(readyQueue.poll());
        }

        readyQueue.addAll(processes);

        return processes;
    }
}
