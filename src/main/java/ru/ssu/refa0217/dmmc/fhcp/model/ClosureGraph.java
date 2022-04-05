package ru.ssu.refa0217.dmmc.fhcp.model;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClosureGraph extends Graph {
    private List<Pair<Integer, Integer>> addedEdges = new ArrayList<>();

    public ClosureGraph(String name, int dimension) {
        super(name, dimension);
    }

    public ClosureGraph(String name, int dimension, Map<Integer, Set<Integer>> adjacencyList) {
        super(name, dimension, adjacencyList);
    }

    @Override
    public void addEdge(int i, int j) {
        super.addEdge(i, j);
        addedEdges.add(Pair.of(i, j));
    }

    public List<Pair<Integer, Integer>> getAddedEdges() {
        return addedEdges;
    }

    public void removeEdge(Integer i, Integer j) {
        getAdjacencyList().get(i).remove(j);
        getAdjacencyList().get(j).remove(i);
    }
}
