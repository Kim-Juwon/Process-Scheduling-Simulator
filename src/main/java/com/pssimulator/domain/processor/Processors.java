package com.pssimulator.domain.processor;

import com.pssimulator.dto.request.ProcessorRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Processors {
    private static final int FRONT = 0;

    private final List<Processor> processors;

    /*
        삭제가 빈번하게 일어나므로 객체 생성시 linked list로 구성한다.
     */

    public static Processors createEmpty() {
        return new Processors(new LinkedList<>());
    }

    public static Processors from(List<ProcessorRequestDto> dtos) {
        List<Processor> processors = dtos.stream()
                .map(Processor::from)
                .collect(Collectors.toCollection(LinkedList::new));

        return new Processors(processors);
    }

    public static Processors fromProcessors(List<Processor> processorList) {
        if (processorList == null) {
            return new Processors(new LinkedList<>());
        }

        return new Processors(new LinkedList<>(processorList));
    }

    public boolean isEmpty() {
        return processors.isEmpty();
    }

    public void addProcessors(Processors processors) {
        this.processors.addAll(processors.getProcessors());
    }

    public void changeToRequiredStartupPower() {
        processors.forEach(Processor::changeToRequiredStartupPower);
    }

    public Processor getNextProcessor() {
        if (processors.isEmpty()) {
            return null;
        }

        Processor nextProcessor = getFrontProcessor();
        removeFrontProcessor();

        return nextProcessor;
    }

    private Processor getFrontProcessor() {
        return processors.get(FRONT);
    }

    private void removeFrontProcessor() {
        processors.remove(FRONT);
    }
}
