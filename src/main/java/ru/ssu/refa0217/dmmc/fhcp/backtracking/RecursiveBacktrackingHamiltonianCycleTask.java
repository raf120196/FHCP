package ru.ssu.refa0217.dmmc.fhcp.backtracking;

import ru.ssu.refa0217.dmmc.fhcp.model.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecursiveBacktrackingHamiltonianCycleTask extends AbstractBacktrackingHamiltonianCycleTask {
    private final List<Integer> cycle;

    public RecursiveBacktrackingHamiltonianCycleTask(Graph graph, Integer firstNode, Integer secondNode) {
        super(graph, firstNode, secondNode);
        cycle = new ArrayList<>(graph.getDimension());
        cycle.add(firstNode);
        cycle.add(secondNode);
    }

    @Override
    public BacktrackingResult call() {
        System.out.println(Thread.currentThread().getName() + ": started for " + cycle);
        return findCycle(2);
    }

    private BacktrackingResult findCycle(int index) {
        if (Thread.currentThread().isInterrupted()) {
            return new BacktrackingResult(BacktrackingResult.BacktrackingResultCode.INTERRUPTED);
        }
        if (index == graph.getDimension()) {
            if (graph.hasEdge(cycle.get(index - 1), cycle.get(0))) {
                return new BacktrackingResult(true, cycle);
            } else {
                return new BacktrackingResult(false);
            }
        }
        Set<Integer> nextNodeCandidates = graph.getAdjacencyList().get(cycle.get(index - 1));
        for (Integer nextNodeCandidate : nextNodeCandidates) {
            if (!cycle.contains(nextNodeCandidate)) {
                cycle.add(nextNodeCandidate);
                BacktrackingResult result = findCycle(index + 1);
                if (result.isInterrupted()) {
                    return result;
                }
                if (result.isResult()) {
                    return result;
                } else {
                    cycle.remove(cycle.size() - 1);
                }
            }
        }
        return new BacktrackingResult(false);
    }
}
