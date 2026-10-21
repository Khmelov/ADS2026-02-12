package by.it.group551004.zubko.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(line);
            }
        }
        String input = sb.toString();
        if (input.isEmpty()) return;

        Map<String, Set<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                adj.putIfAbsent(from, new TreeSet<>());
                adj.putIfAbsent(to, new TreeSet<>());
                inDegree.putIfAbsent(from, 0);
                inDegree.putIfAbsent(to, 0);

                if (adj.get(from).add(to)) {
                    inDegree.put(to, inDegree.get(to) + 1);
                }
            }
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
            for (String v : adj.getOrDefault(u, Collections.emptySet())) {
                inDegree.put(v, inDegree.get(v) - 1);
                if (inDegree.get(v) == 0) {
                    pq.add(v);
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}
