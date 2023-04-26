package com.pssimulator.scheduler;

import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.processor.Processors;
import com.pssimulator.domain.queue.ReadyQueue;
import com.pssimulator.domain.status.RunningStatus;
import com.pssimulator.dto.request.AlgorithmDto;
import com.pssimulator.dto.request.Request;
import com.pssimulator.dto.response.Response;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Scheduler {
    protected final ReadyQueue readyQueue;
    protected final Processes notArrivedProcesses;
    protected final Processors availableProcessors;
    protected final RunningStatus runningStatus;

    public abstract Response schedule(Request request);

    public static Scheduler from(Request request) {
        AlgorithmDto algorithm = request.getAlgorithm();

        if (algorithm.equals(AlgorithmDto.FCFS)) {
            return FCFSScheduler.from(request);
        }
        if (algorithm.equals(AlgorithmDto.RR)) {
            return RRScheduler.from(request);
        }
        if (algorithm.equals(AlgorithmDto.SPN)) {
            return SPNScheduler.from(request);
        }
        if (algorithm.equals(AlgorithmDto.SRTN)) {
            return SRTNScheduler.from(request);
        }
        if (algorithm.equals(AlgorithmDto.HRRN)) {
            return HRRNScheduler.from(request);
        }
        
        return null;
    }
}
