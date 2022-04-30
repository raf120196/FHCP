package ru.ssu.refa0217.dmmc.fhcp.model;

import ru.ssu.refa0217.dmmc.fhcp.hybridham.Path;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private final int idx;
    private final String name;
    private final int dimension;
    private final Map<Integer, Set<Integer>> adjacencyList;

    public Graph(int idx, String name, int dimension) {
        this.idx = idx;
        this.name = name;
        this.dimension = dimension;
        adjacencyList = new HashMap<>();
    }

    public Graph(int idx, String name, int dimension, Map<Integer, Set<Integer>> adjacencyList) {
        this.idx = idx;
        this.name = name;
        this.dimension = dimension;
        this.adjacencyList = adjacencyList;
    }

    public int getIdx() {
        return idx;
    }

    public String getName() {
        return name;
    }

    public int getDimension() {
        return dimension;
    }

    public Map<Integer, Set<Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public int getNodeDegree(Integer node) {
        Set<Integer> neighbors = adjacencyList.get(node);
        if (neighbors == null) {
            return 0;
        }
        return neighbors.size();
    }

    public Set<Integer> getNodeNeighbors(Integer node) {
        Set<Integer> neighbors = adjacencyList.get(node);
        if (neighbors == null) {
            return Collections.emptySet();
        }
        return neighbors;
    }

    public Set<Integer> getUnvisitedNodeNeighbors(Integer node, Path path) {
        return getNodeNeighbors(node)
                .stream()
                .filter(neighbor -> !path.containsNode(neighbor))
                .collect(Collectors.toSet());
    }

    public boolean hasEdge(int i, int j) {
        Set<Integer> connectedNodes = adjacencyList.get(i);
        return connectedNodes != null && connectedNodes.contains(j);
    }

    public void addEdge(int i, int j) {
        adjacencyList.computeIfAbsent(i, v -> new HashSet<>()).add(j);
        adjacencyList.computeIfAbsent(j, v -> new HashSet<>()).add(i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Graph graph = (Graph) o;
        return dimension == graph.dimension &&
                name.equals(graph.name) &&
                adjacencyList.equals(graph.adjacencyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dimension, adjacencyList);
    }

    @Override
    public String toString() {
        return "Graph{" +
                "name='" + name + '\'' +
                ", dimension=" + dimension +
                ", adjacencyList=" + adjacencyList +
                '}';
    }
}
