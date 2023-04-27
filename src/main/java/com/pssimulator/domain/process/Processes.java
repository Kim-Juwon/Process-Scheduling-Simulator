package com.pssimulator.domain.process;

import com.pssimulator.domain.time.IntegerTime;
import com.pssimulator.dto.request.ProcessRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Processes {
    private final List<Process> processes;

    public static Processes from(List<ProcessRequestDto> dtos) {
        List<Process> processes = dtos.stream()
                .map(Process::from)
                .collect(Collectors.toCollection(LinkedList::new));

        return new Processes(processes);
    }

    public static Processes fromProcesses(List<Process> processList) {
        if (processList == null) {
            return new Processes(new LinkedList<>());
        }

        return new Processes(new LinkedList<>(processList));
    }

    public static Processes createEmpty() {
        return new Processes(new LinkedList<>());
    }

    public boolean isEmpty() {
        return processes.isEmpty();
    }

    public int getSize() {
        return processes.size();
    }

    public List<Process> getArrivedProcessesAt(IntegerTime time) {
        List<Process> arrivedProcesses = new ArrayList<>();

        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            if (process.hasSameArrivalTime(time)) {
                arrivedProcesses.add(process);
                processes.remove(i);
                i--;
            }
        }

        return arrivedProcesses;
    }

    public void calculateResult() {
        processes.forEach(Process::calculateResult);
    }

    public void initializeRunningBurstTime() {
        processes.forEach(Process::initializeRunningBurstTime);
    }
}
