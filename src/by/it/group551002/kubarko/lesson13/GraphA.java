package by.it.group551002.kubarko.lesson13;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            input.append(input.length() == 0 ? "" : ", ").append(scanner.nextLine());
        }

        Map<String, TreeSet<String>> graph = new TreeMap<>();
        Map<String, Integer> inDegree = new TreeMap<>();
        for (String edge : input.toString().split(",")) {
            String trimmed = edge.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            String[] vertices = trimmed.split("\\s*->\\s*");
            if (vertices.length != 2) {
                throw new IllegalArgumentException("Invalid edge: " + trimmed);
            }
            String from = vertices[0].trim();
            String to = vertices[1].trim();
            graph.computeIfAbsent(from, ignored -> new TreeSet<>());
            graph.computeIfAbsent(to, ignored -> new TreeSet<>());
            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);
            if (graph.get(from).add(to)) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        PriorityQueue<String> ready = new PriorityQueue<>();
        for (Map.Entry<String, Integer> vertex : inDegree.entrySet()) {
            if (vertex.getValue() == 0) {
                ready.add(vertex.getKey());
            }
        }

        StringBuilder result = new StringBuilder();
        int processed = 0;
        while (!ready.isEmpty()) {
            String vertex = ready.remove();
            processed++;
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(vertex);
            for (String next : graph.get(vertex)) {
                int remaining = inDegree.get(next) - 1;
                inDegree.put(next, remaining);
                if (remaining == 0) {
                    ready.add(next);
                }
            }
        }

        if (processed != graph.size()) {
            throw new IllegalArgumentException("Graph contains a cycle");
        }
        System.out.println(result);
    }
}
