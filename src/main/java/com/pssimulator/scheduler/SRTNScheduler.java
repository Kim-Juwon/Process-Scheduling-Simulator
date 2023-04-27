package com.pssimulator.scheduler;

import com.pssimulator.domain.pair.Pair;
import com.pssimulator.domain.pair.Pairs;
import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.processor.Processor;
import com.pssimulator.domain.processor.Processors;
import com.pssimulator.domain.queue.SRTNReadyQueue;
import com.pssimulator.domain.status.RunningStatus;
import com.pssimulator.dto.request.Request;
import com.pssimulator.dto.response.Response;

public class SRTNScheduler extends Scheduler {
    private SRTNScheduler(SRTNReadyQueue srtnReadyQueue, Processes processes, Processors processors, RunningStatus runningStatus) {
        super(srtnReadyQueue, processes, processors, runningStatus);
    }

    public static SRTNScheduler from(Request request) {
        return new SRTNScheduler(
                SRTNReadyQueue.createEmpty(),
                Processes.from(request.getProcesses()),
                Processors.from(request.getProcessors()),
                RunningStatus.create()
        );
    }

    @Override
    public Response schedule(Request request) {
        Response response = Response.create();

        while (isRemainingProcessExist()) {
            addArrivedProcessesToReadyQueue();

            Integer preemptedProcessesSize = null;
            if (isRunningProcessExist()) {
                if (isTerminatedRunningProcessExist()) {
                    Processes terminatedRunningProcesses = getTerminatedRunningProcesses();
                    Processors terminatedProcessors = getTerminatedRunningProcessors();
                    removeTerminatedPairsFromRunningStatus();

                    terminatedRunningProcesses.calculateResult();

                    response.addTerminatedProcessesFrom(terminatedRunningProcesses);
                    bringProcessorsBackFrom(terminatedProcessors);
                }
                if (isPreemptibleProcessExist()) {
                    Pairs preemptedPairs = preempt();
                    Processes preemptedProcesses = preemptedPairs.getProcesses();
                    Processors preemptedProcessors = preemptedPairs.getProcessors();

                    preemptedProcessesSize = preemptedProcesses.getSize();

                    addProcessesToReadyQueueFrom(preemptedProcesses);
                    bringProcessorsBackFrom(preemptedProcessors);
                }
            }

            if (isProcessExistInReadyQueue()) {
                if (preemptedProcessesSize == null) {
                    assignProcessorsToProcessesAndRegisterToRunningStatus();
                } else {
                    assignProcessorsToProcessesAndRegisterToRunningStatusUpto(preemptedProcessesSize);
                }
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

    private void addProcessesToReadyQueueFrom(Processes preemptedProcesses) {
        SRTNReadyQueue srtnReadyQueue = (SRTNReadyQueue) readyQueue;
        srtnReadyQueue.addPreemptedProcesses(preemptedProcesses);
    }

    private boolean isTerminatedRunningProcessExist() {
        return runningStatus.isTerminatedProcessExist();
    }

    private boolean isPreemptibleProcessExist() {
        return runningStatus.isLessRemainingWorkloadProcessExistIn(readyQueue);
    }

    private Pairs preempt() {
        return runningStatus.getBiggerRemainingWorkloadPairsComparedWith(readyQueue);
    }

    private Processes getTerminatedRunningProcesses() {
        return runningStatus.getTerminatedProcesses();
    }

    private Processors getTerminatedRunningProcessors() {
        return runningStatus.getTerminatedProcessors();
    }

    private void removeTerminatedPairsFromRunningStatus() {
        runningStatus.removeTerminatedPairs();
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

    private void assignProcessorsToProcessesAndRegisterToRunningStatusUpto(int size) {
        int assignedCount = 1;

        while (isAvailableProcessorExist() && assignedCount <= size) {
            if (isReadyQueueEmpty()) {
                break;
            }

            Processor nextProcessor = getNextAvailableProcessor();
            Process nextProcess = getNextReadyProcess();
            changeToRunningStatus(Pair.of(nextProcess, nextProcessor));

            assignedCount++;
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
