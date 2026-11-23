package by.it.group551001.romanovich.lesson13;

import java.io.IOException;

public class GraphB {
    static boolean hasCycle(DirectedGraph graph) {
        return graph.topologicalOrder().size() != graph.outgoing.size();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(hasCycle(DirectedGraph.read()) ? "yes" : "no");
    }
}
