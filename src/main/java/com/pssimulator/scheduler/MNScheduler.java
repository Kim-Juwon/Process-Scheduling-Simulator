package com.pssimulator.scheduler;

import com.pssimulator.domain.constant.ProgramConstants;
import com.pssimulator.domain.pair.Pair;
import com.pssimulator.domain.pair.Pairs;
import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.processor.Processor;
import com.pssimulator.domain.processor.Processors;
import com.pssimulator.domain.queue.MNReadyQueue;
import com.pssimulator.domain.status.RunningStatus;
import com.pssimulator.domain.time.IntegerTime;
import com.pssimulator.dto.request.Request;
import com.pssimulator.dto.response.Response;

public class MNScheduler extends Scheduler {
    private final IntegerTime timeQuantum;
    private final Double remainingWorkloadBaselineRatio;

    private MNScheduler(MNReadyQueue mnReadyQueue, Processes processes, Processors processors, RunningStatus runningStatus, IntegerTime timeQuantum, Double remainingWorkloadBaselineRatio) {
        super(mnReadyQueue, processes, processors, runningStatus);
        this.timeQuantum = timeQuantum;
        this.remainingWorkloadBaselineRatio = remainingWorkloadBaselineRatio;
    }

    public static MNScheduler from(Request request) {
        return new MNScheduler(
                MNReadyQueue.createEmpty(),
                Processes.from(request.getProcesses()),
                Processors.from(request.getProcessors()),
                RunningStatus.create(),
                IntegerTime.from(request.getTimeQuantum()),
                ProgramConstants.REMAINING_WORKLOAD_BASELINE_RATIO
        );
    }

    @Override
    public Response schedule(Request request) {
        Response response = Response.create();

        while (isRemainingProcessExist()) {
            addArrivedProcessesToReadyQueue();

            if (isRunningProcessExist()) {
                if (isTerminatedRunningProcessExist()) {
                    Pairs pairs = getTerminatedPairs();
                    Processes terminatedProcesses = pairs.getTerminatedProcesses();
                    Processors terminatedProcessors = pairs.getTerminatedProcessors();

                    calculateResultOfTerminatedProcessesFrom(terminatedProcesses);
                    initializeRunningBurstTimeOfProcessesFrom(terminatedProcesses);

                    bringProcessorsBackFrom(terminatedProcessors);

                    response.addTerminatedProcessesFrom(terminatedProcesses);
                }
                if (isPreemptibleProcessExist()) {
                    Pairs preemptedPairs = preempt();
                    Processes preemptedProcesses = preemptedPairs.getProcesses();
                    Processors preemptedProcessors = preemptedPairs.getProcessors();

                    initializeRunningBurstTimeOfProcessesFrom(preemptedProcesses);
                    ungrantAdditionalTimeOfProcessesFrom(preemptedProcesses);

                    addProcessesToReadyQueueFrom(preemptedProcesses);
                    bringProcessorsBackFrom(preemptedProcessors);
                }
            }

            if (isProcessExistInReadyQueue()) {
                assignProcessorsToProcessesAndRegisterToRunningStatus();
            }

            increaseWaitingTimeOfProcessesInReadyQueue();
            updateWorkloadAndBurstTimeOfRunningProcesses();
            updatePowerConsumption();

            addResultTo(response);
            applyCurrentTimeStatusTo(response);

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
        MNReadyQueue malneonReadyQueue = (MNReadyQueue) readyQueue;
        malneonReadyQueue.addPreemptedProcesses(processes);
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
        return runningStatus.getTimeQuantumExpiredAndNotMalneonPairs(timeQuantum, remainingWorkloadBaselineRatio);
    }

    private void calculateResultOfTerminatedProcessesFrom(Processes terminatedProcesses) {
        terminatedProcesses.calculateResult();
    }

    private void initializeRunningBurstTimeOfProcessesFrom(Processes terminatedProcesses) {
        terminatedProcesses.initializeRunningBurstTime();
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
        processors.changeToRequiredStartupPower();
        availableProcessors.addProcessors(processors);
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
