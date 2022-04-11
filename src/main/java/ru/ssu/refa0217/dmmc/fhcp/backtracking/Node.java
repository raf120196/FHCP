package ru.ssu.refa0217.dmmc.fhcp.backtracking;

import java.util.HashSet;
import java.util.Set;

public class Node {
    private final Integer node;
    private final Set<Integer> consideredNeighbors;

    public Node(Integer node) {
        this.node = node;
        consideredNeighbors = new HashSet<>();
    }

    public Integer getNode() {
        return node;
    }

    public boolean isNeighborConsidered(Integer neighbor) {
        return consideredNeighbors.contains(neighbor);
    }

    public void addConsideredNeighbor(Integer neighbor) {
        consideredNeighbors.add(neighbor);
    }

    @Override
    public String toString() {
        return "Node{" +
                "node=" + node +
                ", consideredNeighbors=" + consideredNeighbors +
                '}';
    }
}
