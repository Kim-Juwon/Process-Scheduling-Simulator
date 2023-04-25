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
    private final String name; // 이름
    private final IntegerTime arrivalTime; // 도착 시간
    private final IntegerTime burstTime; // 총 수행 시간
    private final Workload workload; // 남은 작업량
    private final IntegerTime waitingTime; // 총 대기 시간
    private final IntegerTime turnaroundTime; // waitingTime + burstTime
    private final DoubleTime normalizedTurnaroundTime; // turnaroundTime / burstTime
    private final ResponseRatio responseRatio; // (waitingTime + workload) / workload
    private final Workload initialWorkload; // 최초 workload
    private final IntegerTime runningBurstTime; // preemption 되기 전까지 수행된 시간

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
                .initialWorkload(Workload.from(dto.getWorkload()))
                .runningBurstTime(IntegerTime.createZero())
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

    public boolean isTimeQuantumExpired(IntegerTime timeQuantum) {
        return runningBurstTime.equals(timeQuantum);
    }

    public boolean isRemainingWorkloadBiggerThan(Process process) {
        return workload.isBiggerThan(process.getWorkload());
    }

    public void initializeRunningBurstTime() {
        runningBurstTime.changeToZero();
    }

    public void updateWorkloadAndBurstTimeFrom(Processor processor) {
        workload.decreaseBy(processor.getThroughputPerSecond());
        burstTime.increase();
        runningBurstTime.increase();
    }

    public void calculateResponseRatio() {
        responseRatio.changeFrom(waitingTime, initialWorkload);
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

    public int compareBySRTN(Process process) {
        return workload.compare(process.getWorkload());
    }

    public int compareByHRRN(Process process) {
        return responseRatio.compare(process.getResponseRatio());
    }
}
