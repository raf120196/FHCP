package ru.ssu.refa0217.dmmc.fhcp.bondy_chvatal;

import org.apache.commons.lang3.tuple.Pair;
import ru.ssu.refa0217.dmmc.fhcp.model.ClosureGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BondyChvatalHamiltonianCycleBuilder {
    public static List<Integer> findHamiltonianCycle(ClosureGraph graph) {
        List<Integer> cycle = IntStream.range(0, graph.getDimension()).boxed().collect(Collectors.toList());

        List<Pair<Integer, Integer>> addedEdges = graph.getAddedEdges();
        for (int i = addedEdges.size() - 1; i >= 0; i--) {
            Pair<Integer, Integer> edge = addedEdges.get(i);
            graph.removeEdge(edge.getLeft(), edge.getRight());
            Result cycleContainsEdge = isCycleContainsEdge(cycle, edge);
            if (cycleContainsEdge != Result.NO) {
                Integer pointOfStart = cycleContainsEdge == Result.PLAIN ? edge.getRight() : edge.getLeft();
                int idx = cycle.size() - cycle.indexOf(pointOfStart);
                Collections.rotate(cycle, idx);
                int s = -1;
                for (int j = 1; j < cycle.size() - 2; j++) {
                    Integer a = cycle.get(j);
                    Integer b = cycle.get(j + 1);
                    if (graph.hasEdge(a, cycle.get(cycle.size() - 1)) && graph.hasEdge(b, cycle.get(0))) {
                        s = j;
                        break;
                    }
                }
                if (s == -1) {
                    throw new RuntimeException("Edge is not found!");
                }
                cycle = changeCycle(cycle, s);
            }
        }

        return cycle;
    }

    private static Result isCycleContainsEdge(List<Integer> cycle, Pair<Integer, Integer> edge) {
        for (int i = 0; i < cycle.size(); i++) {
            Integer a = edge.getLeft();
            Integer b = edge.getRight();
            Integer u = cycle.get(i);
            Integer v = i + 1 == cycle.size() ? cycle.get(0) : cycle.get(i + 1);
            if (u.equals(a) && v.equals(b)) {
                return Result.PLAIN;
            } else if (v.equals(a) && u.equals(b)) {
                return Result.BACK;
            }
        }
        return Result.NO;
    }

    private static List<Integer> changeCycle(List<Integer> cycle, int s) {
        List<Integer> newCycle = new ArrayList<>();
        for (int i = 0; i <= s; i++) {
            newCycle.add(cycle.get(i));
        }
        for (int i = cycle.size() - 1; i >= s + 1; i--) {
            newCycle.add(cycle.get(i));
        }
        return newCycle;
    }

    private enum Result {
        NO,
        PLAIN,
        BACK;
    }
}
