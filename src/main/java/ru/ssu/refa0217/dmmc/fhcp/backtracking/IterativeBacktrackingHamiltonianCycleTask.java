package ru.ssu.refa0217.dmmc.fhcp.backtracking;

import ru.ssu.refa0217.dmmc.fhcp.model.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class IterativeBacktrackingHamiltonianCycleTask extends AbstractBacktrackingHamiltonianCycleTask {
    private final List<Node> cycle;
    private final Set<Integer> cycleElements;

    public IterativeBacktrackingHamiltonianCycleTask(Graph graph, Integer firstNode, Integer secondNode) {
        super(graph, firstNode, secondNode);
        cycle = new ArrayList<>(graph.getDimension());
        cycle.add(new Node(firstNode));
        cycle.add(new Node(secondNode));
        cycleElements = new HashSet<>();
        cycleElements.add(firstNode);
        cycleElements.add(secondNode);
    }

    @Override
    public BacktrackingResult call() {
        return findCycle();
    }

    private BacktrackingResult findCycle() {
        int n = graph.getDimension();
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                return new BacktrackingResult(BacktrackingResult.BacktrackingResultCode.INTERRUPTED);
            }
            if (cycle.size() == 1) {
                return new BacktrackingResult(false);
            }
            if (cycle.size() == n) {
                if (graph.hasEdge(cycle.get(n - 1).getNode(), cycle.get(0).getNode())) {
                    return new BacktrackingResult(true, convertToCycle(cycle));
                } else {
                    cycleElements.remove(cycle.get(n - 1).getNode());
                    cycle.remove(n - 1);
                    continue;
                }
            }

            Node node = cycle.get(cycle.size() - 1);
            Optional<Integer> nextNode = findNextCandidate(node);
            if (nextNode.isEmpty()) {
                cycleElements.remove(node.getNode());
                cycle.remove(cycle.size() - 1);
                continue;
            }
            cycle.add(new Node(nextNode.get()));
            cycleElements.add(nextNode.get());
            node.addConsideredNeighbor(nextNode.get());
        }
    }

    private Optional<Integer> findNextCandidate(Node node) {
        Set<Integer> neighbors = graph.getAdjacencyList().get(node.getNode());
        if (neighbors == null) {
            return Optional.empty();
        }
        return neighbors.stream()
                .filter(neighbor -> !node.isNeighborConsidered(neighbor))
                .filter(neighbor -> !cycleElements.contains(neighbor))
                .findAny();
    }

    private List<Integer> convertToCycle(List<Node> nodes) {
        return nodes.stream().map(Node::getNode).collect(Collectors.toList());
    }
}
