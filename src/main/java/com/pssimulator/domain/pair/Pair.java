package com.pssimulator.domain.pair;

import com.pssimulator.domain.process.Process;
import com.pssimulator.domain.processor.Processor;
import lombok.Getter;

@Getter
public class Pair {
    private Process process;
    private Processor processor;
}
