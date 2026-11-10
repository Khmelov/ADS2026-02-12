package by.it.group551004.pedko.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class GraphA {

    private final Map<String, Set<String>> predecessors = new TreeMap<>();

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        GraphA graph = parse(reader.readLine());
        System.out.println(graph.topologicalOrder());
    }

    static GraphA parse(String text) {
        GraphA graph = new GraphA();
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

    String topologicalOrder() {
        StringBuilder order = new StringBuilder();

        while (!predecessors.isEmpty()) {
            String next = null;

            for (Map.Entry<String, Set<String>> vertex : predecessors.entrySet()) {
                if (vertex.getValue().isEmpty()) {
                    next = vertex.getKey();
                    break;
                }
            }
            if (next == null) {
                System.err.println("Граф содержит цикл — полная топологическая сортировка невозможна.");
                break;
            }
            if (!order.isEmpty()) {
                order.append(' ');
            }
            order.append(next);

            predecessors.remove(next);
            for (Set<String> preds : predecessors.values()) {
                preds.remove(next);
            }
        }
        return order.toString();
    }
}