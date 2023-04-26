package com.pssimulator.scheduler;

import com.pssimulator.domain.process.Pair;
import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.processor.Processor;
import com.pssimulator.domain.processor.Processors;
import com.pssimulator.domain.queue.RRReadyQueue;
import com.pssimulator.domain.status.RunningStatus;
import com.pssimulator.domain.time.IntegerTime;
import com.pssimulator.dto.request.Request;
import com.pssimulator.dto.response.Response;

public class RRScheduler extends Scheduler {
    private final IntegerTime timeQuantum;

    private RRScheduler(RRReadyQueue rrReadyQueue, Processes processes, Processors processors, RunningStatus runningStatus, IntegerTime timeQuantum) {
        super(rrReadyQueue, processes, processors, runningStatus);
        this.timeQuantum = timeQuantum;
    }

    public static RRScheduler from(Request request) {
        return new RRScheduler(
                RRReadyQueue.createEmpty(),
                Processes.from(request.getProcesses()),
                Processors.from(request.getProcessors()),
                RunningStatus.create(),
                IntegerTime.from(request.getTimeQuantum())
        );
    }

    @Override
    public Response schedule(Request request) {
        Response response = Response.create();

        while (isRemainingProcessExist()) {
            addArrivedProcessesToReadyQueue();

            if (isRunningProcessExist()) {
                if (isTerminatedRunningProcessExist()) {
                    Processes terminatedRunningProcesses = getTerminatedRunningProcesses();
                    Processors terminatedProcessors = getTerminatedRunningProcessors();
                    removeTerminatedPairsFromRunningStatus();

                    terminatedRunningProcesses.calculateResult();

                    response.addTerminatedProcessesFrom(terminatedRunningProcesses);
                    bringProcessorsBackFrom(terminatedProcessors);
                }
                if (isTimeQuantumExpiredRunningProcessExist()) {
                    Processes timeQuantumExpiredRunningProcesses = getTimeQuantumExpiredRunningProcesses();
                    Processors processorsAboutTimeQuantumExpiredProcesses = getProcessorsAboutTimeQuantumExpiredProcesses();
                    removeTimeQuantumExpiredPairsFromRunningStatus();

                    timeQuantumExpiredRunningProcesses.initializeRunningBurstTime();

                    addPreemptedProcessesToReadyQueueFrom(timeQuantumExpiredRunningProcesses);
                    bringProcessorsBackFrom(processorsAboutTimeQuantumExpiredProcesses);
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

    private void addPreemptedProcessesToReadyQueueFrom(Processes preemptedProcesses) {
        RRReadyQueue rrReadyQueue = (RRReadyQueue) readyQueue;
        rrReadyQueue.addPreemptedProcesses(preemptedProcesses);
    }

    private boolean isTerminatedRunningProcessExist() {
        return runningStatus.isTerminatedProcessExist();
    }

    private boolean isTimeQuantumExpiredRunningProcessExist() {
        return runningStatus.isTimeQuantumExpiredProcessExist(timeQuantum);
    }

    private Processes getTerminatedRunningProcesses() {
        return runningStatus.getTerminatedProcesses();
    }

    private Processors getTerminatedRunningProcessors() {
        return runningStatus.getTerminatedProcessors();
    }

    private Processes getTimeQuantumExpiredRunningProcesses() {
        return runningStatus.getTimeQuantumExpiredProcesses(timeQuantum);
    }

    private Processors getProcessorsAboutTimeQuantumExpiredProcesses() {
        return runningStatus.getProcessorsAboutTimeQuantumExpiredProcesses(timeQuantum);
    }

    private void removeTerminatedPairsFromRunningStatus() {
        runningStatus.removeTerminatedPairs();
    }

    private void removeTimeQuantumExpiredPairsFromRunningStatus() {
        runningStatus.removeTimeQuantumExpiredPairs(timeQuantum);
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
        response.applyCurrentTimeStatus();
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
