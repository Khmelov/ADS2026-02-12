package by.it.group551004.pedko.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GraphB {

    private final Map<String, Set<String>> predecessors = new TreeMap<>();

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        GraphB graph = parse(reader.readLine());
        System.out.println(graph.hasCycle() ? "yes" : "no");
    }

    static GraphB parse(String text) {
        GraphB graph = new GraphB();
        if (text == null || text.isBlank()) {
            return graph;
        }
        for (String edge : text.split(",")) {
            if (edge.isBlank()) {
                continue;
            }
            String[] parts = edge.split("->");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Некорректная дуга: \"" + edge.trim() + "\"");
            }
            graph.addEdge(parts[0].trim(), parts[1].trim());
        }
        return graph;
    }

    void addEdge(String from, String to) {
        predecessors.putIfAbsent(from, new HashSet<>());
        predecessors.computeIfAbsent(to, k -> new HashSet<>()).add(from);
    }

    boolean hasCycle() {
        while (removeVertexWithoutPredecessors()) {}

        return !predecessors.isEmpty();
    }

    private boolean removeVertexWithoutPredecessors() {
        for (Map.Entry<String, Set<String>> vertex : predecessors.entrySet()) {
            if (vertex.getValue().isEmpty()) {
                String removed = vertex.getKey();
                predecessors.remove(removed);
                for (Set<String> preds : predecessors.values()) {
                    preds.remove(removed);
                }
                return true;
            }
        }
        return false;
    }
}