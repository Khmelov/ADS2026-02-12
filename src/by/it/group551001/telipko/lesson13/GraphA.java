package by.it.group551001.telipko.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.hasNextLine() ? scanner.nextLine() : "";

        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        for (String edge : line.split(",")) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new HashSet<>());
            graph.putIfAbsent(to, new HashSet<>());
            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);

            if (graph.get(from).add(to)) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) queue.add(e.getKey());
        }

        StringBuilder result = new StringBuilder();
        while (!queue.isEmpty()) {
            String v = queue.poll();
            if (!result.isEmpty()) result.append(' ');
            result.append(v);
            for (String next : graph.get(v)) {
                int d = inDegree.get(next) - 1;
                inDegree.put(next, d);
                if (d == 0) queue.add(next);
            }
        }

        System.out.println(result);
    }
}
