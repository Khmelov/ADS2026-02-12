package by.it.group551002.efimchikov.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphA {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        if (input == null) input = "";

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        for (String edge : input.split(",")) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;

            String[] parts = edge.split("->");
            if (parts.length != 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());

            indegree.putIfAbsent(from, 0);
            indegree.put(to, indegree.getOrDefault(to, 0) + 1);
        }

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : indegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            result.add(vertex);

            for (String next : graph.getOrDefault(vertex, Collections.emptyList())) {
                int newDegree = indegree.get(next) - 1;
                indegree.put(next, newDegree);

                if (newDegree == 0) {
                    queue.add(next);
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}
