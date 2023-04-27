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
    private final Workload remainingWorkload; // 남은 작업량
    private final IntegerTime waitingTime; // 총 대기 시간
    private final IntegerTime turnaroundTime; // waitingTime + burstTime
    private final DoubleTime normalizedTurnaroundTime; // turnaroundTime / burstTime
    private final ResponseRatio responseRatio; // (waitingTime + workload) / workload
    private final Workload totalWorkload; // 총 해야할 작업량
    private final IntegerTime runningBurstTime; // preemption 되기 전까지 수행된 시간 (Round-Robin 에서 time quantum 만료 여부 사용에 판단)

    public static Process from(ProcessRequestDto dto) {
        return Process.builder()
                .name(dto.getName())
                .arrivalTime(IntegerTime.from(dto.getArrivalTime()))
                .burstTime(IntegerTime.createZero())
                .remainingWorkload(Workload.from(dto.getWorkload()))
                .waitingTime(IntegerTime.createZero())
                .turnaroundTime(IntegerTime.createEmpty())
                .normalizedTurnaroundTime(DoubleTime.createEmpty())
                .responseRatio(ResponseRatio.createEmpty())
                .totalWorkload(Workload.from(dto.getWorkload()))
                .runningBurstTime(IntegerTime.createZero())
                .build();
    }

    public boolean hasSameArrivalTime(IntegerTime time) {
        return arrivalTime.equals(time);
    }

    public boolean isTerminated() {
        return remainingWorkload.isZero();
    }

    public void increaseWaitingTime() {
        waitingTime.increase();
    }

    public boolean isTimeQuantumExpired(IntegerTime timeQuantum) {
        return runningBurstTime.isSameOrHigher(timeQuantum);
    }

    public boolean isRemainingWorkloadBiggerThan(Process process) {
        return remainingWorkload.isBiggerThan(process.getRemainingWorkload());
    }

    public void initializeRunningBurstTime() {
        runningBurstTime.changeToZero();
    }

    public void updateWorkloadAndBurstTimeFrom(Processor processor) {
        decreaseRemainingWorkloadFrom(processor);
        increaseBurstTime();
    }

    private void decreaseRemainingWorkloadFrom(Processor processor) {
        remainingWorkload.decreaseBy(processor.getThroughputPerSecond());
    }

    private void increaseBurstTime() {
        increaseTotalBurstTime();
        increaseRunningBurstTime();
    }

    private void increaseTotalBurstTime() {
        burstTime.increase();
    }

    private void increaseRunningBurstTime() {
        runningBurstTime.increase();
    }

    public void calculateResponseRatio() {
        responseRatio.changeFrom(waitingTime, totalWorkload);
    }

    public void calculateResult() {
        calculateTurnaroundTime();
        calculateNormalizedTurnaroundTime();
    }

    private void calculateTurnaroundTime() {
        // TT = WT + BT
        turnaroundTime.changeTo(waitingTime.add(burstTime));
    }

    private void calculateNormalizedTurnaroundTime() {
        // NTT = TT / BT
        normalizedTurnaroundTime.changeTo(turnaroundTime.divide(burstTime));
    }

    public int compareBySPN(Process process) {
        return remainingWorkload.compareByAscending(process.getRemainingWorkload());
    }

    public int compareBySRTN(Process process) {
        return remainingWorkload.compareByAscending(process.getRemainingWorkload());
    }

    public int compareByHRRN(Process process) {
        return responseRatio.compareByDescending(process.getResponseRatio());
    }

    public int compareByRemainingWorkloadDescending(Process process) {
        return remainingWorkload.compareByDescending(process.getRemainingWorkload());
    }
}
