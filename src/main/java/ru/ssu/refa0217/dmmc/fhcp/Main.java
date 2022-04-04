package ru.ssu.refa0217.dmmc.fhcp;

import ru.ssu.refa0217.dmmc.fhcp.model.Graph;
import ru.ssu.refa0217.dmmc.fhcp.model.GraphReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    private static final String FILE_PATH_PREFIX = "C:\\Users\\rafar\\Desktop\\FHCP\\FHCPCS\\graph";
    private static final String FILE_PATH_SUFFIX = ".hcp";
    private static final int FIRST_GRAPH = 1;
    private static final int LAST_GRAPH = 1001;

    public static void main(String[] args) throws IOException {
        List<Graph> graphs = new ArrayList<>();
        for (int i = FIRST_GRAPH; i <= LAST_GRAPH; i++) {
            Graph graph = GraphReader.readGraph(FILE_PATH_PREFIX + i + FILE_PATH_SUFFIX);
            graphs.add(graph);
        }
        graphs.sort(Comparator.comparingInt(Graph::getDimension));
        graphs.forEach(graph -> System.out.println(graph.getName()));
    }
}
