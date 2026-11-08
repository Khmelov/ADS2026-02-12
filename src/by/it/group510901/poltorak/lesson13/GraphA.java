package by.it.group510901.poltorak.lesson13;

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

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = line.split(",");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                vertices.add(from);
                vertices.add(to);

                adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            }
        }

        for (String v : vertices) {
            inDegree.putIfAbsent(v, 0);
        }

        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : vertices) {
            if (inDegree.get(v) == 0) {
                pq.offer(v);
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String current = pq.poll();
            result.add(current);

            List<String> neighbors = adj.get(current);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    int newDegree = inDegree.get(neighbor) - 1;
                    inDegree.put(neighbor, newDegree);
                    if (newDegree == 0) {
                        pq.offer(neighbor);
                    }
                }
            }
        }
        System.out.println(String.join(" ", result));
    }
}