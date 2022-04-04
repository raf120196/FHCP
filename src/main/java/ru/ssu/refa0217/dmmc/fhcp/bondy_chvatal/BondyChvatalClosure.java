package ru.ssu.refa0217.dmmc.fhcp.bondy_chvatal;

import ru.ssu.refa0217.dmmc.fhcp.model.Graph;

import java.util.HashMap;
import java.util.Map;

public class BondyChvatalClosure {
    public static Graph makeClosure(final Graph graph) {
        int n = graph.getDimension();
        Graph closureGraph = new Graph(graph.getName(), n, graph.getAdjacencyList());
        Map<Integer, Integer> deg = getDegreesMap(closureGraph);
        boolean addPair;
        do {
            addPair = false;
            for (int i = 0; i < n && !addPair; i++) {
                int di = deg.get(i);
                for (int j = i + 1; j < n; j++) {
                    int dj = deg.get(j);
                    if (!closureGraph.hasEdge(i, j) && di + dj >= n) {
                        closureGraph.addEdge(i, j);
                        deg.put(i, di + 1);
                        deg.put(j, dj + 1);
                        addPair = true;
                        break;
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
