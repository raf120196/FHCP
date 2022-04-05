package ru.ssu.refa0217.dmmc.fhcp;

import ru.ssu.refa0217.dmmc.fhcp.bondy_chvatal.BondyChvatalClosure;
import ru.ssu.refa0217.dmmc.fhcp.bondy_chvatal.BondyChvatalHamiltonianCycleBuilder;
import ru.ssu.refa0217.dmmc.fhcp.model.ClosureGraph;
import ru.ssu.refa0217.dmmc.fhcp.model.Graph;
import ru.ssu.refa0217.dmmc.fhcp.model.GraphReader;
import ru.ssu.refa0217.dmmc.fhcp.model.GraphUtils;
import ru.ssu.refa0217.dmmc.fhcp.writer.BondyChvatalSufficiencyConditionMessage;
import ru.ssu.refa0217.dmmc.fhcp.writer.HamiltonianCycleMessage;
import ru.ssu.refa0217.dmmc.fhcp.writer.ResultSaverJob;
import ru.ssu.refa0217.dmmc.fhcp.writer.TerminateMessage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String FILE_PATH_PREFIX = "C:\\Users\\rafar\\Desktop\\FHCP\\FHCPCS\\graph";
    private static final String FILE_PATH_SUFFIX = ".hcp";
    private static final int FIRST_GRAPH = 1;
    private static final int LAST_GRAPH = 1001;

    public static void main(String[] args) throws IOException, InterruptedException {
        List<Graph> graphs = new ArrayList<>();
        for (int i = FIRST_GRAPH; i <= LAST_GRAPH; i++) {
            Graph graph = GraphReader.readGraph(FILE_PATH_PREFIX + i + FILE_PATH_SUFFIX);
            graphs.add(graph);
        }
        ResultSaverJob job = new ResultSaverJob();
        job.start();
        graphs.parallelStream()
                .forEach(graph -> handleGraph(graph, job));

        job.addMessage(new TerminateMessage());
        job.join();
    }

    private static void handleGraph(Graph graph, ResultSaverJob job) {
        System.out.println("Started for " + graph.getName());

        LocalDateTime closureStartDate = LocalDateTime.now();
        ClosureGraph closureGraph = BondyChvatalClosure.makeClosure(graph);
        if (GraphUtils.isGraphComplete(closureGraph)) {
            System.out.println(graph.getName() + " is completed");
            LocalDateTime closureEndDate = LocalDateTime.now();
            job.addMessage(new BondyChvatalSufficiencyConditionMessage(graph.getName(), closureStartDate, closureEndDate));

            LocalDateTime cycleStartDate = LocalDateTime.now();
            List<Integer> hamiltonianCycle = BondyChvatalHamiltonianCycleBuilder.findHamiltonianCycle(closureGraph);
            LocalDateTime cycleEndDate = LocalDateTime.now();
            GraphUtils.validateHamiltonianCycle(graph, hamiltonianCycle);
            job.addMessage(new HamiltonianCycleMessage(graph.getName(), cycleStartDate, cycleEndDate, hamiltonianCycle));
        }
    }
}
