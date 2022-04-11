package ru.ssu.refa0217.dmmc.fhcp.backtracking;

import ru.ssu.refa0217.dmmc.fhcp.model.Graph;

import java.util.concurrent.Callable;

public abstract class AbstractBacktrackingHamiltonianCycleTask implements Callable<BacktrackingResult> {
    protected final Graph graph;
    protected final Integer firstNode;
    protected final Integer secondNode;

    public AbstractBacktrackingHamiltonianCycleTask(Graph graph, Integer firstNode, Integer secondNode) {
        this.graph = graph;
        this.firstNode = firstNode;
        this.secondNode = secondNode;
    }
}
