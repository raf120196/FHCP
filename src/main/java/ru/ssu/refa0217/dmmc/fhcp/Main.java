package ru.ssu.refa0217.dmmc.fhcp;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import ru.ssu.refa0217.dmmc.fhcp.backtracking.BacktrackingHamiltonianCycleBuilder;
import ru.ssu.refa0217.dmmc.fhcp.bondy_chvatal.BondyChvatalClosure;
import ru.ssu.refa0217.dmmc.fhcp.bondy_chvatal.BondyChvatalHamiltonianCycleBuilder;
import ru.ssu.refa0217.dmmc.fhcp.hybridham.HybridHamiltonianCycleBuilder;
import ru.ssu.refa0217.dmmc.fhcp.model.ClosureGraph;
import ru.ssu.refa0217.dmmc.fhcp.model.Graph;
import ru.ssu.refa0217.dmmc.fhcp.model.GraphReader;
import ru.ssu.refa0217.dmmc.fhcp.model.GraphUtils;
import ru.ssu.refa0217.dmmc.fhcp.writer.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final String FILE_PATH_PREFIX = "C:\\Users\\rafar\\Desktop\\FHCP\\FHCPCS\\graph";
    private static final String FILE_PATH_SUFFIX = ".hcp";

    private static final int FIRST_GRAPH = 1;
    private static final int LAST_GRAPH = 1001;
    private static final int LAST_GRAPH_FOR_BACKTRACKING = 4;

    private static final String BONDY_CHVATAL_MODE = "bondy_chvatal";
    private static final String BACKTRACKING_MODE = "backtracking";
    private static final String HYBRID_HAM_MODE = "hybrid_ham";

    private static final AtomicInteger COUNT_OF_PATHS = new AtomicInteger(0);
    private static final AtomicInteger COUNT_OF_CYCLES = new AtomicInteger(0);
    private static final CopyOnWriteArrayList<String> CYCLE_GRAPHS = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
        if (args.length != 1) {
            throw new RuntimeException("Mode were not passed");
        }

        List<Graph> graphs = new ArrayList<>();
        for (int i = FIRST_GRAPH; i <= LAST_GRAPH; i++) {
            Graph graph = GraphReader.readGraph(i, FILE_PATH_PREFIX + i + FILE_PATH_SUFFIX);
            graphs.add(graph);
        }

        ResultSaverJob job = new ResultSaverJob();
        job.setName("Result Saver Thread");
        job.start();

        switch (args[0]) {
            case BONDY_CHVATAL_MODE:
                graphs.parallelStream()
                        .forEach(graph -> handleByBondyChvatalAlgorithm(graph, job));
                break;
            case BACKTRACKING_MODE:
                for (int i = FIRST_GRAPH; i <= LAST_GRAPH_FOR_BACKTRACKING; i++) {
                    Graph graph = graphs.get(i - 1);
                    handleByBacktracking(graph, job);
                }
                break;
            case HYBRID_HAM_MODE:
                graphs.parallelStream()
                        .forEach(graph -> handleByHybridHAM(graph, job));
                System.out.println("Count of paths: " + COUNT_OF_PATHS.get());
                System.out.println("Count of cycles: " + COUNT_OF_CYCLES.get());
                System.out.println(CYCLE_GRAPHS);
        }

        job.addMessage(new TerminateMessage());
        job.join();
    }

    private static void handleByBondyChvatalAlgorithm(Graph graph, ResultSaverJob job) {
        System.out.println("Started for " + graph.getName());

        StopWatch closureWatch = StopWatch.createStarted();
        ClosureGraph closureGraph = BondyChvatalClosure.makeClosure(graph);
        if (GraphUtils.isGraphComplete(closureGraph)) {
            System.out.println(graph.getName() + " is completed");
            closureWatch.stop();
            job.addMessage(new BondyChvatalSufficiencyConditionMessage(graph.getName(), closureWatch));

            StopWatch watch = StopWatch.createStarted();
            List<Integer> hamiltonianCycle = BondyChvatalHamiltonianCycleBuilder.findHamiltonianCycle(closureGraph);
            watch.stop();
            GraphUtils.validateHamiltonianCycle(graph, hamiltonianCycle);
            job.addMessage(new HamiltonianCycleMessage(graph.getName(), watch, hamiltonianCycle));
        }

        System.out.println("Completed for " + graph.getName());
    }

    private static void handleByBacktracking(Graph graph, ResultSaverJob job) throws ExecutionException, InterruptedException {
        System.out.println("Started for " + graph.getName());

        StopWatch watch = StopWatch.createStarted();
        BacktrackingHamiltonianCycleBuilder builder = new BacktrackingHamiltonianCycleBuilder(graph);
        List<Integer> hamiltonianCycle = builder.findHamiltonianCycle();
        watch.stop();
        GraphUtils.validateHamiltonianCycle(graph, hamiltonianCycle);
        job.addMessage(new HamiltonianCycleMessage(graph.getName(), watch, hamiltonianCycle));

        System.out.println("Completed for " + graph.getName());
    }

    private static void handleByHybridHAM(Graph graph, ResultSaverJob resultSaverJob) {
        System.out.println("Started for " + graph.getName());

        StopWatch watch = StopWatch.createStarted();
        HybridHamiltonianCycleBuilder hybridHAM = new HybridHamiltonianCycleBuilder(graph);
        Pair<List<Integer>, List<Integer>> pathAndCycle = hybridHAM.findHamiltonianCycle();
        watch.stop();
        if (pathAndCycle != null && pathAndCycle.getRight() != null) {
            GraphUtils.validateHamiltonianCycle(graph, pathAndCycle.getRight());
        }
        if (pathAndCycle != null) {
            resultSaverJob.addMessage(new HybridHamiltonianCycleMessage(graph.getName(), watch, pathAndCycle));
            COUNT_OF_PATHS.incrementAndGet();
            if (pathAndCycle.getRight() != null) {
                COUNT_OF_CYCLES.incrementAndGet();
                CYCLE_GRAPHS.add(graph.getName());
            }
        }

        System.out.println("Completed for " + graph.getName());
    }
}
