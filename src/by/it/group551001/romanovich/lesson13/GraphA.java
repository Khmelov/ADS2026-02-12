package by.it.group551001.romanovich.lesson13;

import java.io.IOException;
import java.util.List;

public class GraphA {
    static List<String> sort(DirectedGraph graph) {
        List<String> order = graph.topologicalOrder();
        if (order.size() != graph.outgoing.size()) {
            throw new IllegalArgumentException("Topological sorting requires an acyclic graph");
        }
        return order;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(String.join(" ", sort(DirectedGraph.read())));
    }
}
