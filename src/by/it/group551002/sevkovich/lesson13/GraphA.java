package by.it.group551002.sevkovich.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String arg) {
        String[] edges = arg.split(", ");
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);
        }

        // Second pass: build graph and in-degree
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];
            graph.get(from).add(to);
            inDegree.put(to, inDegree.get(to) + 1);
        }

        // Kahn's algorithm
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                pq.offer(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String u = pq.poll();
            result.add(u);
            for (String v : graph.get(u)) {
                inDegree.put(v, inDegree.get(v) - 1);
                if (inDegree.get(v) == 0) {
                    pq.offer(v);
                }
            }
        }

        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
        System.out.println();
    }
}