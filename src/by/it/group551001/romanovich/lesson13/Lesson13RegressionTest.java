package by.it.group551001.romanovich.lesson13;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

public class Lesson13RegressionTest {
    @Test
    public void randomComponentsMatchMutualReachability() {
        Random random = new Random(8123);
        for (int test = 0; test < 150; test++) {
            int n = 2 + random.nextInt(15);
            boolean[][] reachable = new boolean[n][n];
            StringBuilder edges = new StringBuilder();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j || random.nextInt(7) == 0) {
                        if (!edges.isEmpty()) {
                            edges.append(", ");
                        }
                        edges.append(i).append("->").append(j);
                        reachable[i][j] = true;
                    }
                }
            }
            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        reachable[i][j] |= reachable[i][k] && reachable[k][j];
                    }
                }
            }
            DirectedGraph graph = DirectedGraph.parse(edges.toString());
            List<List<String>> components = GraphC.components(graph);
            Map<String, Integer> componentOf = new HashMap<>();
            for (int i = 0; i < components.size(); i++) {
                List<String> component = components.get(i);
                for (int j = 0; j < component.size(); j++) {
                    String vertex = component.get(j);
                    assertNull(componentOf.put(vertex, i));
                    if (j > 0) {
                        assertTrue(component.get(j - 1).compareTo(vertex) < 0);
                    }
                }
            }
            assertEquals(n, componentOf.size());
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int from = componentOf.get(Integer.toString(i));
                    int to = componentOf.get(Integer.toString(j));
                    assertEquals(reachable[i][j] && reachable[j][i], from == to);
                    if (reachable[i][j]) {
                        assertTrue(from <= to);
                    }
                }
            }
        }
    }

    @Test
    public void longChainsAndEmptyGraphs() {
        assertTrue(GraphA.sort(DirectedGraph.parse("  ")).isEmpty());
        assertFalse(GraphB.hasCycle(DirectedGraph.parse("")));
        assertTrue(GraphC.components(DirectedGraph.parse("")).isEmpty());
        StringBuilder edges = new StringBuilder();
        for (int i = 0; i < 15000; i++) {
            if (i > 0) {
                edges.append(',');
            }
            edges.append(i).append("->").append(i + 1);
        }
        DirectedGraph graph = DirectedGraph.parse(edges.toString());
        assertEquals(15001, GraphA.sort(graph).size());
        assertFalse(GraphB.hasCycle(graph));
        assertEquals(15001, GraphC.components(graph).size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void topologicalSortRejectsCycles() {
        GraphA.sort(DirectedGraph.parse("A->B, B->A"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void malformedEdgesAreRejected() {
        DirectedGraph.parse("A->");
    }
}
