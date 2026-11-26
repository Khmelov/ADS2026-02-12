package by.it.group510902.shust.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\\A");
        if (!scanner.hasNext()) return;
        String input = scanner.next();

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        for (String edge : input.split(",")) {
            String[] parts = edge.trim().split("\\s*->\\s*");
            String u = parts[0];
            String v = parts[1];

            adj.putIfAbsent(u, new ArrayList<>());
            adj.putIfAbsent(v, new ArrayList<>());
            inDegree.putIfAbsent(u, 0);
            inDegree.putIfAbsent(v, 0);

            adj.get(u).add(v);
            inDegree.put(v, inDegree.get(v) + 1);
        }

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
            for (String v : adj.get(u)) {
                int deg = inDegree.get(v) - 1;
                inDegree.put(v, deg);
                if (deg == 0) {
                    pq.offer(v);
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}