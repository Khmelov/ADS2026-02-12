package by.it.group551001.romanovich.lesson13;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

final class DirectedGraph {
    final SortedMap<String, SortedSet<String>> outgoing = new TreeMap<>();
    final SortedMap<String, SortedSet<String>> incoming = new TreeMap<>();

    static DirectedGraph read() throws IOException {
        return parse(new String(System.in.readAllBytes(), StandardCharsets.UTF_8));
    }

    static DirectedGraph parse(String input) {
        DirectedGraph graph = new DirectedGraph();
        if (input.isBlank()) {
            return graph;
        }
        for (String edge : input.trim().split("\\s*,\\s*", -1)) {
            String[] vertices = edge.split("\\s*->\\s*", -1);
            if (vertices.length != 2 || vertices[0].isBlank() || vertices[1].isBlank()) {
                throw new IllegalArgumentException("Expected an edge: vertex -> vertex");
            }
            String from = vertices[0].trim();
            String to = vertices[1].trim();
            graph.addVertex(from);
            graph.addVertex(to);
            graph.outgoing.get(from).add(to);
            graph.incoming.get(to).add(from);
        }
        return graph;
    }

    private void addVertex(String vertex) {
        outgoing.computeIfAbsent(vertex, ignored -> new TreeSet<>());
        incoming.computeIfAbsent(vertex, ignored -> new TreeSet<>());
    }

    List<String> topologicalOrder() {
        Map<String, Integer> indegree = new HashMap<>();
        PriorityQueue<String> ready = new PriorityQueue<>();
        for (String vertex : outgoing.keySet()) {
            int degree = incoming.get(vertex).size();
            indegree.put(vertex, degree);
            if (degree == 0) {
                ready.add(vertex);
            }
        }
        List<String> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            String vertex = ready.remove();
            order.add(vertex);
            for (String next : outgoing.get(vertex)) {
                int degree = indegree.get(next) - 1;
                indegree.put(next, degree);
                if (degree == 0) {
                    ready.add(next);
                }
            }
        }
        return order;
    }
}
