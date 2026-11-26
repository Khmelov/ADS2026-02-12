package by.it.group551001.artobolevskiy.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String input = scanner.nextLine();

        Map<String, List<String>> adjMap = new HashMap<>();

        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            if (edge.trim().isEmpty()) continue;
            String[] parts = edge.split("\\s*->\\s*");
            if (parts.length != 2) continue;

            String u = parts[0].trim();
            String v = parts[1].trim();

            adjMap.putIfAbsent(u, new ArrayList<>());
            adjMap.putIfAbsent(v, new ArrayList<>());
            inDegree.putIfAbsent(u, 0);
            inDegree.putIfAbsent(v, 0);

            adjMap.get(u).add(v);
            inDegree.put(v, inDegree.get(v) + 1);
        }

        PriorityQueue<String> pq = new PriorityQueue<>();

        for (String vertex : inDegree.keySet()) {
            if (inDegree.get(vertex) == 0) {
                pq.add(vertex);
            }
        }

        List<String> result = new ArrayList<>();

        // Алгоритм Кана
        while (!pq.isEmpty()) {
            String u = pq.poll();
            result.add(u);

            if (adjMap.containsKey(u)) {
                for (String v : adjMap.get(u)) {
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
