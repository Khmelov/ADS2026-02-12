package by.it.group551002.efimchikov.lesson13;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphB {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        if (input == null) input = "";

        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        for (String edge : input.split(",")) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;

            String[] parts = edge.split("->");
            if (parts.length != 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new HashSet<>());
            graph.computeIfAbsent(to, k -> new HashSet<>());
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
            String vertex = queue.poll();
            processed++;

            for (String next : graph.getOrDefault(vertex, Collections.emptySet())) {
                int newDegree = indegree.get(next) - 1;
                indegree.put(next, newDegree);

                if (newDegree == 0) {
                    queue.add(next);
                }
            }
        }

        System.out.println(processed < indegree.size() ? "yes" : "no");
    }
}