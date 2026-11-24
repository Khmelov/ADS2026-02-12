package by.it.group551004.sheev.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GraphB {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();

        Set<String> vertices = new HashSet<>();
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        if (line != null && !line.trim().isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                String[] parts = edge.trim().split("->");
                String from = parts[0].trim();
                String to = parts[1].trim();
                vertices.add(from);
                vertices.add(to);
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                indegree.merge(to, 1, Integer::sum);
                indegree.putIfAbsent(from, 0);
            }
        }

        for (String v : vertices) {
            indegree.putIfAbsent(v, 0);
        }

        // Алг. Кана
        Queue<String> queue = new LinkedList<>();
        for (String v : vertices) {
            if (indegree.get(v) == 0) {
                queue.add(v);
            }
        }

        int visited = 0;
        while (!queue.isEmpty()) {
            String v = queue.poll();
            visited++;
            List<String> neighbors = graph.get(v);
            if (neighbors != null) {
                for (String u : neighbors) {
                    indegree.merge(u, -1, Integer::sum);
                    if (indegree.get(u) == 0) {
                        queue.add(u);
                    }
                }
            }
        }

        // Если обработали не все вершины — есть цикл
        System.out.println(visited == vertices.size() ? "no" : "yes");
    }
}