package ru.ssu.refa0217.dmmc.fhcp.writer;

import org.apache.commons.lang3.time.StopWatch;

import java.util.List;

public class HamiltonianCycleMessage implements Message {
    private final String graphName;
    private final StopWatch watch;
    private final List<Integer> hamiltonianCycle;

    public HamiltonianCycleMessage(String graphName, StopWatch watch, List<Integer> hamiltonianCycle) {
        this.graphName = graphName;
        this.watch = watch;
        this.hamiltonianCycle = hamiltonianCycle;
    }

    public String getGraphName() {
        return graphName;
    }

    public StopWatch getWatch() {
        return watch;
    }

    public List<Integer> getHamiltonianCycle() {
        return hamiltonianCycle;
    }
}
