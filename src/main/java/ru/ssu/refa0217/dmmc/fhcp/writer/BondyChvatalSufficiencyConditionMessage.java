package ru.ssu.refa0217.dmmc.fhcp.writer;

import java.time.LocalDateTime;

public class BondyChvatalSufficiencyConditionMessage implements Message {
    private final String graphName;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public BondyChvatalSufficiencyConditionMessage(String graphName, LocalDateTime startDate, LocalDateTime endDate) {
        this.graphName = graphName;
        this.startDate = startDate;
        this.endDate = endDate;
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
}
