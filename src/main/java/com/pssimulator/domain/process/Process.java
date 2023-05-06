package com.pssimulator.domain.process;

import com.pssimulator.domain.constant.ProgramConstants;
import com.pssimulator.domain.grant.Grant;
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
    private final Grant additionalTimeGranted; // 시간이 한번 더 부여됐는지 여부 (말년병장 알고리즘에서 사용)

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
                .additionalTimeGranted(Grant.createFalse())
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

    public boolean isMalneon() {
        double remainingWorkloadRatio = (double) remainingWorkload.getWorkload() / totalWorkload.getWorkload();
        return remainingWorkloadRatio <= ProgramConstants.REMAINING_WORKLOAD_BASELINE_RATIO;
    }

    public boolean isAdditionalTimeGranted() {
        return additionalTimeGranted.isGranted();
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

    public void grantAdditionalTime() {
        additionalTimeGranted.grant();
    }

    public void ungrantAdditionalTime() {
        additionalTimeGranted.ungrant();
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

    public int compareByRemainingWorkloadAscending(Process process) {
        return remainingWorkload.compareByAscending(process.getRemainingWorkload());
    }
}
