package ru.ssu.refa0217.dmmc.fhcp.writer;

import java.time.LocalDateTime;
import java.util.List;

public class HamiltonianCycleMessage implements Message {
    private final String graphName;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final List<Integer> hamiltonianCycle;

    public HamiltonianCycleMessage(String graphName, LocalDateTime startDate, LocalDateTime endDate, List<Integer> hamiltonianCycle) {
        this.graphName = graphName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hamiltonianCycle = hamiltonianCycle;
    }

    public String getGraphName() {
        return graphName;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public List<Integer> getHamiltonianCycle() {
        return hamiltonianCycle;
    }
}
