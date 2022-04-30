package ru.ssu.refa0217.dmmc.fhcp.writer;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class HybridHamiltonianCycleMessage implements Message {
    private final String graphName;
    private final StopWatch watch;
    private final Pair<List<Integer>, List<Integer>> pathAndCycle;

    public HybridHamiltonianCycleMessage(String graphName, StopWatch watch, Pair<List<Integer>, List<Integer>> pathAndCycle) {
        this.graphName = graphName;
        this.watch = watch;
        this.pathAndCycle = pathAndCycle;
    }

    public String getGraphName() {
        return graphName;
    }

    public StopWatch getWatch() {
        return watch;
    }

    public Pair<List<Integer>, List<Integer>> getPathAndCycle() {
        return pathAndCycle;
    }
}
