package ru.ssu.refa0217.dmmc.fhcp.backtracking;

import ru.ssu.refa0217.dmmc.fhcp.model.Graph;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class BacktrackingHamiltonianCycleBuilder {
    private final Graph graph;

    public BacktrackingHamiltonianCycleBuilder(Graph graph) {
        this.graph = graph;
    }

    public List<Integer> findHamiltonianCycle() throws ExecutionException, InterruptedException {
        Integer startNode = findStartDegreeNode();
        List<FutureTask<BacktrackingResult>> tasks = new ArrayList<>();
        Set<Integer> nextNodes = graph.getAdjacencyList().get(startNode);
        for (Integer nextNode : nextNodes) {
            if (!nextNode.equals(startNode)) {
                tasks.add(new FutureTask<>(new RecursiveBacktrackingHamiltonianCycleTask(graph, startNode, nextNode)));
            }
        }

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (FutureTask<BacktrackingResult> task : tasks) {
            executorService.execute(task);
        }

        FutureTask<BacktrackingResult> completedTask = null;
        do {
            for (FutureTask<BacktrackingResult> task : tasks) {
                if (task.isDone() && task.get().isResult()) {
                    System.out.println("Решение найдено");
                    completedTask = task;
                    break;
                }
            }
        } while (completedTask == null);

        for (FutureTask<BacktrackingResult> anotherTask : tasks) {
            if (!completedTask.equals(anotherTask)) {
                anotherTask.cancel(true);
            }
        }
        executorService.shutdown();
        return completedTask.get().getCycle();
    }

    private Integer findStartDegreeNode() {
        Optional<Integer> integer = graph.getAdjacencyList().entrySet().stream()
                .filter(entry -> entry.getValue().size() % Runtime.getRuntime().availableProcessors() == 0)
                .min(Comparator.comparingInt(entry -> entry.getValue().size()))
                .map(Map.Entry::getKey);
        return integer.orElseGet(() -> graph.getAdjacencyList().entrySet().stream()
                .min(Comparator.comparingInt(entry -> entry.getValue().size()))
                .map(Map.Entry::getKey)
                .get());
    }
}
