package by.it.group510902.sorokoletov.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextLine()) {
            return;
        }

        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("no");
            return;
        }

        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        String[] edges = input.split("\\s*,\\s*");

        for (String edge : edges) {
            if (edge.isEmpty()) {
                continue;
            }

            String[] parts = edge.split("\\s*->\\s*");
            if (parts.length != 2) {
                continue;
            }

            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new TreeSet<>());
            graph.computeIfAbsent(to, k -> new TreeSet<>());

            indegree.putIfAbsent(from, 0);
            indegree.putIfAbsent(to, 0);

            if (graph.get(from).add(to)) {
                indegree.put(to, indegree.get(to) + 1);
            }
        }


        Deque<String> queue = new ArrayDeque<>();
        for (Map.Entry<String, Integer> entry : indegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        int processed = 0;
        while (!queue.isEmpty()) {
            String current = queue.poll();
            processed++;

            for (String next : graph.getOrDefault(current, Collections.emptySet())) {
                indegree.put(next, indegree.get(next) - 1);
                if (indegree.get(next) == 0) {
                    queue.add(next);
                }
            }
        }

        System.out.println(processed == indegree.size() ? "no" : "yes");
    }
}