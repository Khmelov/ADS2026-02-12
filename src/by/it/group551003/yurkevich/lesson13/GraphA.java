package by.it.group551003.yurkevich.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;

        String line = sc.nextLine().trim();
        Map<String, List<String>> graph = new TreeMap<>();
        Map<String, Integer> indegree = new TreeMap<>();

        if (!line.isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                String[] parts = edge.split("->");
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                graph.computeIfAbsent(to, k -> new ArrayList<>());
                indegree.putIfAbsent(from, 0);
                indegree.putIfAbsent(to, 0);
                indegree.put(to, indegree.get(to) + 1);
            }
        }

        PriorityQueue<String> pq = new PriorityQueue<>();
        for (Map.Entry<String, Integer> e : indegree.entrySet()) {
            if (e.getValue() == 0) {
                pq.offer(e.getKey());
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String v = pq.poll();
            result.add(v);
            for (String next : graph.getOrDefault(v, Collections.emptyList())) {
                indegree.put(next, indegree.get(next) - 1);
                if (indegree.get(next) == 0) {
                    pq.offer(next);
                }
            }
        }

        if (result.size() != indegree.size()) {
            System.out.println("Цикл в графе — топологическая сортировка невозможна");
        } else {
            System.out.println(String.join(" ", result));
        }
    }
}