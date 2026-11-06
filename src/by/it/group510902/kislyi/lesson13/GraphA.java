package by.it.group510902.kislyi.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();

        TreeSet<String> vertices = new TreeSet<>();
        List<String[]> edges = new ArrayList<>();

        if (!line.isEmpty()) {
            String[] parts = line.split(",");
            for (String part : parts) {
                part = part.trim();
                if (part.isEmpty()) continue;
                String[] halves = part.split("->");
                String from = halves[0].trim();
                String to = halves[1].trim();
                vertices.add(from);
                vertices.add(to);
                edges.add(new String[]{from, to});
            }
        }

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();
        for (String v : vertices) {
            adj.put(v, new ArrayList<>());
            indegree.put(v, 0);
        }
        for (String[] e : edges) {
            adj.get(e[0]).add(e[1]);
            indegree.put(e[1], indegree.get(e[1]) + 1);
        }


        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : vertices) {
            if (indegree.get(v) == 0) {
                pq.add(v);
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String v = pq.poll();
            result.add(v);
            for (String next : adj.get(v)) {
                indegree.put(next, indegree.get(next) - 1);
                if (indegree.get(next) == 0) {
                    pq.add(next);
                }
            }
        }

        if (result.size() != vertices.size()) {
            System.out.println("Граф содержит цикл, топологическая сортировка невозможна");
        } else {
            System.out.println(String.join(" ", result));
        }
    }
}
