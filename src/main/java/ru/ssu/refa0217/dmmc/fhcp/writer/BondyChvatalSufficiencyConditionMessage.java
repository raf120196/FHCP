package ru.ssu.refa0217.dmmc.fhcp.writer;

import org.apache.commons.lang3.time.StopWatch;

public class BondyChvatalSufficiencyConditionMessage implements Message {
    private final String graphName;
    private final StopWatch watch;

    public BondyChvatalSufficiencyConditionMessage(String graphName, StopWatch watch) {
        this.graphName = graphName;
        this.watch = watch;
    }

    public String getGraphName() {
        return graphName;
    }

    public StopWatch getWatch() {
        return watch;
    }
}
