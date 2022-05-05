package ru.ssu.refa0217.dmmc.fhcp.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphUtils {
    public static boolean isGraphComplete(final Graph graph) {
        int n = graph.getDimension();
        for (Map.Entry<Integer, Set<Integer>> entry : graph.getAdjacencyList().entrySet()) {
            if (entry.getValue().size() != n - 1) {
                return false;
            }
        }
        return true;
    }

    public static void validateHamiltonianCycle(final Graph graph, List<Integer> hamiltonianCycle) {
        if (new HashSet<>(hamiltonianCycle).size() != graph.getDimension()) {
            throw new RuntimeException("Cycle is not full. Graph dimension = " + graph.getDimension() + ", cycle size = " + hamiltonianCycle.size());
        }
        for (int i = 0; i < graph.getDimension(); i++) {
            if (!hamiltonianCycle.contains(i)) {
                throw new RuntimeException("Cycle doesn't contain node " + (i + 1));
            }
        }
        for (int i = 0; i < hamiltonianCycle.size(); i++) {
            int nextIdx = i + 1 == hamiltonianCycle.size() ? 0 : i + 1;
            if (!graph.hasEdge(hamiltonianCycle.get(i), hamiltonianCycle.get(nextIdx))) {
                throw new RuntimeException("Graph has no edge " + (hamiltonianCycle.get(i) + 1) + " -> " + (hamiltonianCycle.get(nextIdx) + 1));
            }
        }
    }
}
