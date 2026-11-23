package by.it.group551001.zlotnikov.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            return;
        }

        Map<String, Set<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            if (parts.length != 2) {
                continue;
            }
            String u = parts[0].trim();
            String v = parts[1].trim();
            adj.putIfAbsent(u, new HashSet<>());
            adj.putIfAbsent(v, new HashSet<>());
            inDegree.putIfAbsent(u, 0);
            inDegree.putIfAbsent(v, 0);
            if (adj.get(u).add(v)) {
                inDegree.put(v, inDegree.get(v) + 1);
            }
        }

        PriorityQueue<String> pq = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                pq.add(entry.getKey());
            }
        }

        StringBuilder result = new StringBuilder();
        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(u);
            for (String v : adj.get(u)) {
                int deg = inDegree.get(v) - 1;
                inDegree.put(v, deg);
                if (deg == 0) {
                    pq.add(v);
                }
            }
        }

        System.out.println(result);
    }
}
