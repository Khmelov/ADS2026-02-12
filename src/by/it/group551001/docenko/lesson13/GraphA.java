package by.it.group551001.docenko.lesson13;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.TreeSet;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Set<String>> graph = parseGraph(scanner.nextLine());
        System.out.println(topologicalSort(graph));
    }

    private static Map<String, Set<String>> parseGraph(String line) {
        Map<String, Set<String>> graph = new TreeMap<>();
        for (String edge : line.trim().split("\\s*,\\s*")) {
            String[] vertices = edge.split("\\s*->\\s*");
            graph.computeIfAbsent(vertices[0], key -> new TreeSet<>()).add(vertices[1]);
            graph.computeIfAbsent(vertices[1], key -> new TreeSet<>());
        }
        return graph;
    }

    private static String topologicalSort(Map<String, Set<String>> graph) {
        Map<String, Integer> inDegree = new HashMap<>();
        for (String vertex : graph.keySet()) {
            inDegree.putIfAbsent(vertex, 0);
            for (String next : graph.get(vertex)) {
                inDegree.merge(next, 1, Integer::sum);
            }
        }

        PriorityQueue<String> ready = new PriorityQueue<>();
        for (String vertex : graph.keySet()) {
            if (inDegree.get(vertex) == 0) {
                ready.add(vertex);
            }
        }

        StringJoiner order = new StringJoiner(" ");
        while (!ready.isEmpty()) {
            String vertex = ready.poll();
            order.add(vertex);
            for (String next : graph.get(vertex)) {
                if (inDegree.merge(next, -1, Integer::sum) == 0) {
                    ready.add(next);
                }
            }
        }
        return order.toString();
    }
}
