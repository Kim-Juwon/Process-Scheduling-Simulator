package com.pssimulator.domain.process;

import com.pssimulator.domain.processor.Processor;
import com.pssimulator.domain.ratio.ResponseRatio;
import com.pssimulator.domain.time.DoubleTime;
import com.pssimulator.domain.time.IntegerTime;
import com.pssimulator.domain.workload.Workload;
import com.pssimulator.dto.request.ProcessRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class Process {
    private final String name;
    private final IntegerTime arrivalTime;
    private final IntegerTime burstTime;
    private final Workload workload;
    private final IntegerTime waitingTime;
    private final IntegerTime turnaroundTime;
    private final DoubleTime normalizedTurnaroundTime;
    private final ResponseRatio responseRatio;

    public static Process from(ProcessRequestDto dto) {
        return Process.builder()
                .name(dto.getName())
                .arrivalTime(IntegerTime.from(dto.getArrivalTime()))
                .burstTime(IntegerTime.createZero())
                .workload(Workload.from(dto.getWorkload()))
                .waitingTime(IntegerTime.createZero())
                .turnaroundTime(IntegerTime.createEmpty())
                .normalizedTurnaroundTime(DoubleTime.createEmpty())
                .responseRatio(ResponseRatio.createEmpty())
                .build();
    }

    public boolean hasSameArrivalTime(IntegerTime time) {
        return arrivalTime.equals(time);
    }

    public boolean isTerminated() {
        return workload.isZero();
    }

    public void increaseWaitingTime() {
        waitingTime.increase();
    }

    public void updateWorkloadAndBurstTimeFrom(Processor processor) {
        workload.decreaseBy(processor.getThroughputPerSecond());
        burstTime.increase();
    }

    public void calculateResult() {
        calculateTurnaroundTime();
        calculateNormalizedTurnaroundTime();
    }

    private void calculateTurnaroundTime() {
        // TT = WT + BT
        IntegerTime calculatedTime = waitingTime.add(burstTime);
        turnaroundTime.changeTo(calculatedTime);
    }

    private void calculateNormalizedTurnaroundTime() {
        // NTT = TT / BT
        DoubleTime calculatedTime = turnaroundTime.divide(burstTime);
        normalizedTurnaroundTime.changeTo(calculatedTime);
    }

    public int compareBySPN(Process process) {
        return workload.compare(process.getWorkload());
    }
}
