package ru.ssu.refa0217.dmmc.fhcp.hybridham;

import org.apache.commons.lang3.tuple.Pair;
import ru.ssu.refa0217.dmmc.fhcp.model.Graph;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HybridHamiltonianCycleBuilder {
    private final Graph graph;
    private final Map<Integer, Integer> nodeToNodeDegree;
    private final SecureRandom rnd;

    public HybridHamiltonianCycleBuilder(Graph graph) {
        this.graph = graph;
        nodeToNodeDegree = IntStream.range(0, graph.getDimension())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), graph::getNodeDegree));
        try {
            rnd = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Pair<List<Integer>, List<Integer>> findHamiltonianCycle() {
        Path hamiltonianPath = null;
        for (int i = 0; i < 10; i++) {
            List<Path> initialPaths = findInitialPaths();
            for (Path path : initialPaths) {
                Set<UsedRotation> usedRotations = new HashSet<>();
                while (path.getSize() < graph.getDimension()) {
                    if (!rotationalTransform(path, usedRotations)) {
                        break;
                    }
                }
                if (path.getSize() == graph.getDimension()) {
                    hamiltonianPath = path;
                    break;
                }
            }
            if (hamiltonianPath != null) {
                break;
            }
        }
        if (hamiltonianPath == null) {
            return null;
        }

        Path hamiltonianCycle = new Path(hamiltonianPath);
        Set<UsedRotation> usedRotations = new HashSet<>();
        while (!graph.hasEdge(hamiltonianCycle.getFirstNode(), hamiltonianCycle.getLastNode())) {
            if (!convertToCycle(hamiltonianCycle, usedRotations)) {
                break;
            }
        }
        if (graph.hasEdge(hamiltonianCycle.getFirstNode(), hamiltonianCycle.getLastNode())) {
            return Pair.of(hamiltonianPath.getPath(), hamiltonianCycle.getPath());
        }
        return Pair.of(hamiltonianPath.getPath(), null);
    }

    private List<Path> findInitialPaths() {
        int maxDegree = nodeToNodeDegree.values().stream().max(Comparator.naturalOrder()).get();
        List<Integer> maxDegreeNodes = IntStream.range(0, graph.getDimension())
                .boxed()
                .filter(node -> nodeToNodeDegree.get(node) == maxDegree)
                .collect(Collectors.toList());
        List<Path> initialPaths = new ArrayList<>();
        for (Integer firstNode : maxDegreeNodes) {
            Path path = new Path(graph.getDimension());
            Map<Integer, Set<Integer>> nodeToUnvisitedNodes = IntStream.range(0, graph.getDimension())
                    .boxed()
                    .collect(Collectors.toMap(Function.identity(), node -> new HashSet<>(graph.getNodeNeighbors(node))));
            addNodeToPath(path, firstNode, nodeToUnvisitedNodes);
            greedyDfs(firstNode, path, nodeToUnvisitedNodes, false);
            initialPaths.add(path);
        }
        int maxSize = initialPaths.stream().max(Comparator.comparingInt(Path::getSize)).map(Path::getSize).get();
        return initialPaths.stream().filter(path -> path.getSize() == maxSize).collect(Collectors.toList());
    }

    private void addNodeToPath(Path path, Integer node, Map<Integer, Set<Integer>> nodeToUnvisitedNodes) {
        path.addNode(node);
        nodeToUnvisitedNodes.forEach((key, value) -> value.remove(node));
    }

    private void greedyDfs(Integer node, Path path, Map<Integer, Set<Integer>> nodeToUnvisitedNodes, boolean skipHeuristic) {
        List<Integer> unvisitedNeighbors = new ArrayList<>(graph.getUnvisitedNodeNeighbors(node, path));
        List<Integer> goodNeighbors = unvisitedNeighbors
                .stream()
                .filter(neighbor -> isSatisfiedUnreachableVertexHeuristic(neighbor, nodeToUnvisitedNodes))
                .collect(Collectors.toList());
        Integer nextNode = null;
        if (!goodNeighbors.isEmpty()) {
            nextNode = goodNeighbors.get(Math.abs(rnd.nextInt() % goodNeighbors.size()));
        } else if (skipHeuristic && unvisitedNeighbors.size() > 0) {
            nextNode = unvisitedNeighbors.get(Math.abs(rnd.nextInt() % unvisitedNeighbors.size()));
        }
        if (nextNode == null) {
            return;
        }
        addNodeToPath(path, nextNode, nodeToUnvisitedNodes);
        greedyDfs(nextNode, path, nodeToUnvisitedNodes, skipHeuristic);
    }

    private boolean isSatisfiedUnreachableVertexHeuristic(Integer node, Map<Integer, Set<Integer>> nodeToUnvisitedNodes) {
        return nodeToUnvisitedNodes.get(node).stream().allMatch(neighbor -> nodeToUnvisitedNodes.get(neighbor).size() > 1);
    }

    private boolean rotationalTransform(Path path, Set<UsedRotation> usedRotations) {
        reversePathIfNeeded(path);
        boolean result = false;
        List<Integer> points = graph.getNodeNeighbors(path.getLastNode())
                .stream()
                .filter(path::containsNode)
                .collect(Collectors.toList());
        for (Integer point : points) {
            int idx = path.indexOf(point) + 1;
            Integer nodeAtIdx = path.getNodeAt(idx);
            if (nodeAtIdx.equals(path.getLastNode())) {
                continue;
            }
            List<Integer> subPath = path.getSubPath(idx);
            List<Integer> reversedSubPath = new ArrayList<>(subPath);
            Collections.reverse(reversedSubPath);
            UsedRotation usedRotation1 = new UsedRotation(point, subPath);
            UsedRotation usedRotation2 = new UsedRotation(point, reversedSubPath);
            if (!usedRotations.contains(usedRotation1) && !usedRotations.contains(usedRotation2)) {
                usedRotations.add(usedRotation1);
                usedRotations.add(usedRotation2);
                path.reversePartOfPath(idx);
                Map<Integer, Set<Integer>> nodeToUnvisitedNodes = IntStream.range(0, graph.getDimension())
                        .boxed()
                        .collect(Collectors.toMap(Function.identity(), node -> new HashSet<>(graph.getNodeNeighbors(node))));
                greedyDfs(path.getLastNode(), path, nodeToUnvisitedNodes, true);
                result = true;
                break;

            }
        }
        return result;
    }

    private boolean convertToCycle(Path path, Set<UsedRotation> usedRotations) {
        reversePathIfNeeded(path);
        boolean result = false;
        Set<Integer> points = graph.getNodeNeighbors(path.getLastNode());
        for (Integer point : points) {
            int idx = path.indexOf(point) + 1;
            Integer nodeAtIdx = path.getNodeAt(idx);
            if (nodeAtIdx.equals(path.getLastNode())) {
                continue;
            }
            List<Integer> subPath = path.getSubPath(idx);
            List<Integer> reversedSubPath = new ArrayList<>(subPath);
            Collections.reverse(reversedSubPath);
            UsedRotation usedRotation1 = new UsedRotation(point, subPath);
            UsedRotation usedRotation2 = new UsedRotation(point, reversedSubPath);
            if (!usedRotations.contains(usedRotation1) && !usedRotations.contains(usedRotation2)) {
                usedRotations.add(usedRotation1);
                usedRotations.add(usedRotation2);
                path.reversePartOfPath(idx);
                result = true;
                break;
            }
        }
        return result;
    }

    private void reversePathIfNeeded(Path path) {
        int firstNodeDegree = nodeToNodeDegree.get(path.getFirstNode());
        int lastNodeDegree = nodeToNodeDegree.get(path.getLastNode());
        if (firstNodeDegree > lastNodeDegree || firstNodeDegree == lastNodeDegree && (rnd.nextInt() & 1) == 0) {
            path.reverse();
        }
    }

    private static class UsedRotation {
        private final Integer point;
        private final List<Integer> subList;

        private UsedRotation(Integer point, List<Integer> subList) {
            this.point = point;
            this.subList = subList;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UsedRotation usedRotation = (UsedRotation) o;
            return point.equals(usedRotation.point) &&
                    subList.equals(usedRotation.subList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(point, subList);
        }
    }
}
