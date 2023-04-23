package com.pssimulator.scheduler;

import com.pssimulator.domain.process.Pair;
import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.process.Processes;
import com.pssimulator.domain.processor.Processor;
import com.pssimulator.domain.processor.Processors;
import com.pssimulator.domain.queue.FCFSReadyQueue;
import com.pssimulator.domain.status.RunningStatus;
import com.pssimulator.dto.request.Request;
import com.pssimulator.dto.response.Response;

public class FCFSScheduler extends Scheduler {
    private FCFSScheduler(FCFSReadyQueue fcfsReadyQueue, Processes processes,
                          Processors processors, RunningStatus runningStatus) {
        super(fcfsReadyQueue, processes, processors, runningStatus);
    }

    public static FCFSScheduler from(Request request) {
        return new FCFSScheduler(
                FCFSReadyQueue.createEmpty(),
                Processes.from(request.getProcesses()),
                Processors.from(request.getProcessors()),
                RunningStatus.create()
        );
    }

    public Response schedule(Request request) {
        Response response = Response.create();

        while (isRemainingProcessExist()) {
            addArrivedProcessesToReadyQueue();

            if (isRunningProcessExist()) {
                if (isTerminatedRunningProcessExist()) {
                    Processes terminatedRunningProcesses = getTerminatedRunningProcesses();
                    Processors terminatedProcessors = getTerminatedRunningProcessors();
                    removeTerminatedProcessesAndProcessorsFromRunningStatus();

                    terminatedRunningProcesses.calculateResult();

                    response.addTerminatedProcessesFrom(terminatedRunningProcesses);
                    bringProcessorsBackFrom(terminatedProcessors);
                }
            }

            if (isProcessExistInReadyQueue()) {
                assignProcessorsToProcessesAndRegisterToRunningStatus();
            }

            increaseWaitingTimeOfProcessesInReadyQueue();
            updateWorkloadAndBurstTimeOfRunningProcesses();
            updatePowerConsumption();

            response.addPairs(runningStatus.getPairs());
            response.addTotalPowerConsumption(runningStatus.getTotalPowerConsumption());
            response.addReadyQueue(readyQueue);

            response.applyCurrentTimeStatus();
            runningStatus.increaseCurrentTime();
        }

        return response;
    }

    private boolean isRemainingProcessExist() {
        return !isNotArrivedProcessesEmpty() || !isReadyQueueEmpty() || !isRunningProcessEmpty();
    }

    private void addArrivedProcessesToReadyQueue() {
        readyQueue.addArrivedProcessesFrom(notArrivedProcesses, runningStatus.getCurrentTime());
    }

    private boolean isRunningProcessExist() {
        return runningStatus.isProcessesExist();
    }

    private boolean isTerminatedRunningProcessExist() {
        return runningStatus.isTerminatedProcessExist();
    }

    private Processes getTerminatedRunningProcesses() {
        return runningStatus.getTerminatedProcesses();
    }

    private Processors getTerminatedRunningProcessors() {
        return runningStatus.getTerminatedProcessors();
    }

    private void removeTerminatedProcessesAndProcessorsFromRunningStatus() {
        runningStatus.removeTerminatedPairs();
    }

    private boolean isNotArrivedProcessesEmpty() {
        return notArrivedProcesses.isEmpty();
    }

    private boolean isReadyQueueEmpty() {
        return readyQueue.isEmpty();
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
            Processor nextProcessor = getNextAvailableProcessor();

            if (isReadyQueueEmpty()) {
                break;
            }

            availableProcessors.removeFront();
            Process readyProcess = getNextReadyProcess();
            runningStatus.addPair(Pair.of(readyProcess, nextProcessor));
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

    private void increaseWaitingTimeOfProcessesInReadyQueue() {
        readyQueue.increaseWaitingTimeOfProcesses();
    }

    private void updateWorkloadAndBurstTimeOfRunningProcesses() {
        runningStatus.updateWorkloadAndBurstTimeOfProcesses();
    }

    private void updatePowerConsumption() {
        runningStatus.updatePowerConsumption();
    }
}
