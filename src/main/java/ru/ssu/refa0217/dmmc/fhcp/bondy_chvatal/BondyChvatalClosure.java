package ru.ssu.refa0217.dmmc.fhcp.bondy_chvatal;

import ru.ssu.refa0217.dmmc.fhcp.model.ClosureGraph;
import ru.ssu.refa0217.dmmc.fhcp.model.Graph;

import java.util.HashMap;
import java.util.Map;

public class BondyChvatalClosure {
    public static ClosureGraph makeClosure(final Graph graph) {
        int n = graph.getDimension();
        ClosureGraph closureGraph = new ClosureGraph(graph.getIdx(), graph.getName(), n, graph.getAdjacencyList());
        Map<Integer, Integer> deg = getDegreesMap(closureGraph);
        boolean addPair;
        do {
            addPair = false;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (!closureGraph.hasEdge(i, j) && deg.get(i) + deg.get(j) >= n) {
                        closureGraph.addEdge(i, j);
                        deg.put(i, deg.get(i) + 1);
                        deg.put(j, deg.get(j) + 1);
                        addPair = true;
                    }
                }
            }
        } while (addPair);
        return closureGraph;
    }

    private static Map<Integer, Integer> getDegreesMap(final Graph graph) {
        Map<Integer, Integer> degreesMap = new HashMap<>();
        for (int i = 0; i < graph.getDimension(); i++) {
            if (graph.getAdjacencyList().containsKey(i)) {
                degreesMap.put(i, graph.getAdjacencyList().get(i).size());
            } else {
                degreesMap.put(i, 0);
            }
        }
        return degreesMap;
    }
}
