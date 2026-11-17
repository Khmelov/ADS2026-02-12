package by.it.group551003.yakushev.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new TreeMap<>(); // TreeMap для сбора всех вершин
        Set<String> vertices = new TreeSet<>();

        if (!line.isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                String[] parts = edge.trim().split("->");
                String from = parts[0].trim();
                String to = parts[1].trim();

                adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                inDegree.merge(to, 1, Integer::sum);
                inDegree.putIfAbsent(from, 0);
                vertices.add(from);
                vertices.add(to);
            }
        }

        // Инициализация степеней захода для вершин без входящих ребер
        for (String v : vertices) {
            inDegree.putIfAbsent(v, 0);
        }

        // PriorityQueue обеспечивает выбор лексикографически меньшей вершины
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

            for (String v : adj.getOrDefault(u, Collections.emptyList())) {
                int newDeg = inDegree.get(v) - 1;
                inDegree.put(v, newDeg);
                if (newDeg == 0) {
                    pq.offer(v);
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}