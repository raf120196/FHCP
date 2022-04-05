package ru.ssu.refa0217.dmmc.fhcp.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class GraphReader {
    private static final String NAME_LEFT_PART = "NAME : ";
    private static final String DIMENSION_LEFT_PART = "DIMENSION : ";
    private static final String END_OF_FILE = "-1";

    public static Graph readGraph(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String name = br.readLine().replace(NAME_LEFT_PART, "");
            br.readLine();
            br.readLine();
            int dimension = Integer.parseInt(br.readLine().replace(DIMENSION_LEFT_PART, ""));
            br.readLine();
            br.readLine();
            Graph graph = new Graph(name, dimension);
            for (String line; !END_OF_FILE.equals(line = br.readLine()); ) {
                String[] edgeStr = line.split(" ");
                int i = Integer.parseInt(edgeStr[0]) - 1;
                int j = Integer.parseInt(edgeStr[1]) - 1;
                graph.addEdge(i, j);
            }
            return graph;
        }
    }
}
