package com.pssimulator.scheduler;

import com.pssimulator.domain.pair.Pair;
import com.pssimulator.domain.pair.Pairs;
import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.processor.Processor;
import com.pssimulator.domain.processor.Processors;
import com.pssimulator.domain.queue.MNReadyQueue;
import com.pssimulator.domain.queue.Preemptible;
import com.pssimulator.domain.status.RunningStatus;
import com.pssimulator.domain.time.IntegerTime;
import com.pssimulator.dto.request.Request;
import com.pssimulator.dto.response.Response;

public class MNScheduler extends Scheduler {
    private final IntegerTime timeQuantum;

    private MNScheduler(MNReadyQueue mnReadyQueue, Processes processes, Processors processors, RunningStatus runningStatus, IntegerTime timeQuantum) {
        super(mnReadyQueue, processes, processors, runningStatus);
        this.timeQuantum = timeQuantum;
    }

    public static MNScheduler from(Request request) {
        return new MNScheduler(
                MNReadyQueue.createEmpty(),
                Processes.from(request.getProcesses()),
                Processors.from(request.getProcessors()),
                RunningStatus.create(),
                IntegerTime.from(request.getTimeQuantum())
        );
    }

    @Override
    public Response schedule(Request request) {
        Response response = Response.create();

        // 아직 도착하지 않은 프로세스, ready state 프로세스, running state 프로세스 중 1개라도 존재할경우 스케줄링 계속 진행
        while (isRemainingProcessExist()) {
            // 현재 시간에 도착한 프로세스가 있다면 ready queue에 삽입
            addArrivedProcessesToReadyQueue();

            // running state인 프로세스가 있다면
            if (isRunningProcessExist()) {
                // running state 프로세스중 현재 시간에 종료된 프로세스가 있다면
                if (isTerminatedRunningProcessExist()) {
                    // 종료된 프로세스들 및 해당 프로세스들에 할당되었던 프로세서들의 정보를 가져옴
                    Pairs pairs = getTerminatedPairs();
                    Processes terminatedProcesses = pairs.getTerminatedProcesses();
                    Processors terminatedProcessors = pairs.getTerminatedProcessors();

                    // 종료된 프로세스들의 TT와 NTT 계산
                    calculateResultOfTerminatedProcessesFrom(terminatedProcesses);

                    // 종료된 프로세스들에게 할당되었던 프로세서들을 회수
                    bringProcessorsBackFrom(terminatedProcessors);

                    // 응답 객체에 현재 시간에 종료된 프로세스들 정보를 저장
                    response.addTerminatedProcessesFrom(terminatedProcesses);
                }

                // 선점당할 수 있는(time quantum만큼의 제한시간이 만료된) 프로세스가 있다면
                if (isPreemptibleProcessExist()) {
                    // 선점당할 프로세스들(말년병장 알고리즘의 정책에 맞는) 및 해당 프로세스들에 할당되었던 프로세서들을 가져옴
                    Pairs preemptedPairs = preempt();
                    Processes preemptedProcesses = preemptedPairs.getProcesses();
                    Processors preemptedProcessors = preemptedPairs.getProcessors();

                    // 선점당할 프로세스들의 running burst time 초기화 (burst time X)
                    initializeRunningBurstTimeOfProcessesFrom(preemptedProcesses);

                    // time quantum 만큼의 시간이 한번 더 부여된 여부를 false로 변경
                    ungrantAdditionalTimeOfProcessesFrom(preemptedProcesses);

                    // 선점당할 프로세스들을 ready queue에 차례로 삽입
                    addProcessesToReadyQueueFrom(preemptedProcesses);

                    // 종료된 프로세스들에게 할당되었던 프로세서들을 회수
                    bringProcessorsBackFrom(preemptedProcessors);
                }
            }

            // ready queue에 프로세스가 존재한다면
            if (isProcessExistInReadyQueue()) {
                // 가용 가능한 프로세서들을 최대한 프로세스들에게 할당하고 running state로 전이
                assignProcessorsToProcessesAndRegisterToRunningStatus();
            }

            // 쉬고있는 프로세서들은 다음 작업시 시동전력이 필요하다고 변경
            changeAvailableProcessorsToRequireStartupPower();

            // 프로세스들의 WT, BT, 작업량 계산 및 프로세서들의 누적 전력 소비량 계산
            increaseWaitingTimeOfProcessesInReadyQueue();
            updateWorkloadAndBurstTimeOfRunningProcesses();
            updatePowerConsumption();

            // 현재 시간의 스케줄링 상태를 응답 객체에 저장하고, 현재 시간에 대한 상태 적용은 완료되었다고 알림
            addResultTo(response);
            applyCurrentTimeStatusTo(response);

            // 현재 시간을 1초 증가시킴
            increaseCurrentTime();
        }

        return response;
    }

    private boolean isRemainingProcessExist() {
        return !isNotArrivedProcessesEmpty() || !isReadyQueueEmpty() || !isRunningProcessEmpty();
    }

    private void addArrivedProcessesToReadyQueue() {
        readyQueue.addArrivedProcessesFrom(notArrivedProcesses, runningStatus.getCurrentTime());
    }

    private void addProcessesToReadyQueueFrom(Processes processes) {
        ((Preemptible) readyQueue).addPreemptedProcesses(processes);
    }

    private boolean isTerminatedRunningProcessExist() {
        return runningStatus.isTerminatedProcessExist();
    }

    private Pairs getTerminatedPairs() {
        return runningStatus.getTerminatedPairs();
    }

    private boolean isPreemptibleProcessExist() {
        return runningStatus.isTimeQuantumExpiredProcessExist(timeQuantum);
    }

    private Pairs preempt() {
        return runningStatus.getMNPreemptionConditionPairs(timeQuantum);
    }

    private void calculateResultOfTerminatedProcessesFrom(Processes terminatedProcesses) {
        terminatedProcesses.calculateResult();
    }

    private void initializeRunningBurstTimeOfProcessesFrom(Processes processes) {
        processes.initializeRunningBurstTime();
    }

    private void ungrantAdditionalTimeOfProcessesFrom(Processes processes) {
        processes.ungrantAdditionalTime();
    }

    private boolean isNotArrivedProcessesEmpty() {
        return notArrivedProcesses.isEmpty();
    }

    private boolean isReadyQueueEmpty() {
        return readyQueue.isEmpty();
    }

    private boolean isRunningProcessExist() {
        return !runningStatus.isProcessesEmpty();
    }
    private boolean isRunningProcessEmpty() {
        return runningStatus.isProcessesEmpty();
    }

    private void bringProcessorsBackFrom(Processors processors) {
        availableProcessors.addProcessors(processors);
    }

    private void changeAvailableProcessorsToRequireStartupPower() {
        availableProcessors.changeToRequiredStartupPower();
    }

    private boolean isProcessExistInReadyQueue() {
        return !readyQueue.isEmpty();
    }

    private void assignProcessorsToProcessesAndRegisterToRunningStatus() {
        while (isAvailableProcessorExist()) {
            if (isReadyQueueEmpty()) {
                break;
            }

            Processor nextProcessor = getNextAvailableProcessor();
            Process nextProcess = getNextReadyProcess();
            changeToRunningStatus(Pair.of(nextProcess, nextProcessor));
        }
    }

    private boolean isAvailableProcessorExist() {
        return !availableProcessors.isEmpty();
    }

    private Processor getNextAvailableProcessor() {
        return availableProcessors.getNextProcessor();
    }

    private Process getNextReadyProcess() {
        return readyQueue.getNextProcess();
    }

    private void changeToRunningStatus(Pair pair) {
        runningStatus.addPair(pair);
    }

    private void increaseWaitingTimeOfProcessesInReadyQueue() {
        readyQueue.increaseWaitingTimeOfProcesses();
    }

    private void updateWorkloadAndBurstTimeOfRunningProcesses() {
        runningStatus.updateWorkloadAndBurstTimeOfProcesses();
    }

    private void updatePowerConsumption() {
        runningStatus.updatePowerConsumption();
    }

    private void addResultTo(Response response) {
        response.addRunningStateFrom(runningStatus.getPairs(), availableProcessors);
        response.addProcessorPowerConsumptionsFrom(getAllProcessors());
        response.addTotalPowerConsumptionFrom(runningStatus.getTotalPowerConsumption());
        response.addReadyQueueFrom(readyQueue);
    }

    private void applyCurrentTimeStatusTo(Response response) {
        response.apply();
    }

    private Processors getAllProcessors() {
        Processors allProcessors = Processors.createEmpty();
        allProcessors.addProcessors(availableProcessors);
        allProcessors.addProcessors(runningStatus.getProcessors());

        return allProcessors;
    }

    private void increaseCurrentTime() {
        runningStatus.increaseCurrentTime();
    }
}
