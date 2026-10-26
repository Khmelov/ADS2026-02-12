package by.it.group551003.popko.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphA {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> vertices = new TreeSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length != 2) continue;
            String u = parts[0].trim();
            String v = parts[1].trim();

            vertices.add(u);
            vertices.add(v);

            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            inDegree.put(v, inDegree.getOrDefault(v, 0) + 1);
        }

        for (String v : vertices) {
            inDegree.putIfAbsent(v, 0);
        }

        PriorityQueue<String> pq = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                pq.add(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String u = pq.poll();
            result.add(u);

            if (adj.containsKey(u)) {
                List<String> neighbors = new ArrayList<>(adj.get(u));
                Collections.sort(neighbors);
                for (String v : neighbors) {
                    inDegree.put(v, inDegree.get(v) - 1);
                    if (inDegree.get(v) == 0) {
                        pq.add(v);
                    }
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}