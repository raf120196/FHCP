package ru.ssu.refa0217.dmmc.fhcp.bondy_chvatal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.ssu.refa0217.dmmc.fhcp.model.Graph;

@RunWith(PowerMockRunner.class)
class BondyChvatalClosureTest {
    private Graph graph;
    private Graph closureGraph;

    public BondyChvatalClosureTest() {
    }

    @Before
    public void setUp() {
        graph = new Graph("1", 7);
        graph.addEdge(0, 3);
        graph.addEdge(0, 4);
        graph.addEdge(0, 5);
        graph.addEdge(0, 6);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(1, 5);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);

        closureGraph = new Graph("1", 7);
        closureGraph.addEdge(0, 3);
        closureGraph.addEdge(0, 4);
        closureGraph.addEdge(0, 5);
        closureGraph.addEdge(0, 6);
        closureGraph.addEdge(1, 3);
        closureGraph.addEdge(1, 4);
        closureGraph.addEdge(1, 5);
        closureGraph.addEdge(2, 3);
        closureGraph.addEdge(2, 4);
        closureGraph.addEdge(0, 1); // 1st iteration
        closureGraph.addEdge(0, 2); // 2nd iteration
        closureGraph.addEdge(1, 2); // 3rd iteration
    }

    @Test
    public void makeClosure() {
        Graph result = BondyChvatalClosure.makeClosure(graph);
        Assert.assertEquals(closureGraph, result);
    }
}
