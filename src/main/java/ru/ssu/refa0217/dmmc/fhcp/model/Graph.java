package ru.ssu.refa0217.dmmc.fhcp.model;

import java.util.*;

public class Graph {
    private final String name;
    private final int dimension;
    private final Map<Integer, SortedSet<Integer>> adjacencyList;

    public Graph(String name, int dimension) {
        this.name = name;
        this.dimension = dimension;
        adjacencyList = new HashMap<>();
    }

    public Graph(String name, int dimension, Map<Integer, SortedSet<Integer>> adjacencyList) {
        this.name = name;
        this.dimension = dimension;
        this.adjacencyList = adjacencyList;
    }

    public String getName() {
        return name;
    }

    public int getDimension() {
        return dimension;
    }

    public Map<Integer, SortedSet<Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public boolean hasEdge(int i, int j) {
        return adjacencyList.containsKey(i) && adjacencyList.get(i).contains(j);
    }

    public void addEdge(int i, int j) {
        adjacencyList.computeIfAbsent(i, v -> new TreeSet<>()).add(j);
        adjacencyList.computeIfAbsent(j, v -> new TreeSet<>()).add(i);
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
