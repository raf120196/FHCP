package ru.ssu.refa0217.dmmc.fhcp.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class GraphUtilsTest {
    private Graph graph1;
    private Graph graph2;

    @Before
    public void setUp() throws Exception {
        graph1 = new Graph(1, "1", 7);
        graph1.addEdge(0, 1);
        graph1.addEdge(0, 2);
        graph1.addEdge(0, 3);
        graph1.addEdge(0, 4);
        graph1.addEdge(0, 5);
        graph1.addEdge(0, 6);
        graph1.addEdge(1, 2);
        graph1.addEdge(1, 3);
        graph1.addEdge(1, 4);
        graph1.addEdge(1, 5);
        graph1.addEdge(1, 6);
        graph1.addEdge(2, 3);
        graph1.addEdge(2, 4);
        graph1.addEdge(2, 5);
        graph1.addEdge(2, 6);
        graph1.addEdge(3, 4);
        graph1.addEdge(3, 5);
        graph1.addEdge(3, 6);
        graph1.addEdge(4, 5);
        graph1.addEdge(4, 6);
        graph1.addEdge(5, 6);

        graph2 = new Graph(2, "2", 7);
        graph2.addEdge(0, 1);
        graph2.addEdge(0, 2);
        graph2.addEdge(0, 3);
        graph2.addEdge(0, 4);
        graph2.addEdge(0, 5);
        graph2.addEdge(0, 6);
        graph2.addEdge(1, 2);
        graph2.addEdge(1, 3);
        graph2.addEdge(1, 4);
        graph2.addEdge(1, 5);
        graph2.addEdge(1, 6);
        graph2.addEdge(2, 3);
        graph2.addEdge(2, 4);
        graph2.addEdge(2, 5);
        graph2.addEdge(2, 6);
        graph2.addEdge(3, 4);
        graph2.addEdge(3, 5);
        graph2.addEdge(3, 6);
        graph2.addEdge(4, 6);
        graph2.addEdge(5, 6);
    }

    @Test
    public void isGraph1Complete() {
        Assert.assertTrue(GraphUtils.isGraphComplete(graph1));
    }

    @Test
    public void isGraph2NotComplete() {
        Assert.assertFalse(GraphUtils.isGraphComplete(graph2));
    }
}
